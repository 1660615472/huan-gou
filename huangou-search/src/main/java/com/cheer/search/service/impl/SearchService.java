package com.cheer.search.service.impl;

import com.cheer.huangou.common.vo.PageResult;
import com.cheer.huangou.model.*;
import com.cheer.search.client.BrandClient;
import com.cheer.search.client.CategoryClient;
import com.cheer.search.client.GoodsClient;
import com.cheer.search.client.SpecificationClient;
import com.cheer.search.pojo.Goods;
import com.cheer.search.pojo.SearchRequest;
import com.cheer.search.pojo.SearchResult;
import com.cheer.search.repository.GoodsRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.shib.java.lib.utils.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author zhangjie
 * @title:查询数据库，封装成goods对象
 * @data
 */

@Service
public class SearchService {
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SpecificationClient specificationClient;
    @Autowired
    private GoodsRepository goodsRepository; //索引库的增删改查对象

    //json转换工具
    private ObjectMapper mapper = new ObjectMapper ();


    //封装商品信息，插入索引库
    public Goods buildGoods(Spu spu) throws Exception {
        //查询分类
        List<Category> categories = categoryClient.queryCategoryByIds ( Arrays.asList ( spu.getCid1 (), spu.getCid2 (), spu.getCid3 () ) );

        //查询分类名称
        List<String> categoryNames = categories.stream ().map ( Category::getName ).collect ( Collectors.toList () );

        //查询商品详情
        SpuDetail spuDetail = this.goodsClient.queryDetailById ( spu.getId () );

        //查询品牌
        Brand brand = brandClient.queryBrandByBid ( spu.getBrandId () );
        if (brand == null) {
            throw new Exception ( "BRAND_NOT_FOUND" );
        }

        //根据spuId查询所有sku
        List<Sku> skus = goodsClient.querySkuBySpuId ( spu.getId () );
        //初始化一个价格集合，收集所有sku的价格
        List<Long> priceList = new ArrayList<> ();

        //对skus处理，过滤不需要的字段,list里存放map，一个map对应一个sku
        List<Map<String, Object>> newSkus = new ArrayList<> ();
        //遍历老sku集合，复制新的简化版的sku集合




        for (Sku sku : skus) {
            Map<String, Object> map = new HashMap ();
            map.put ( "id", sku.getId () ); //搜索的sku中只需要这4个字段
            map.put ( "title", sku.getTitle () );
            map.put ( "price", sku.getPrice () );
            //image 是以多张图片逗号隔开的字符串形式，只需要逗号前的第一张图片
            //StringUtils.substringBefore ( sku.getImages (),"," )); 截取第一张图片
            map.put("image", StringUtils.isBlank(sku.getImages()) ? "" : StringUtils.split(sku.getImages(),",")[0]);
            newSkus.add ( map );

            priceList.add ( sku.getPrice () );
        }

        //多余的遍历使系统性能变差，所以直接在上面遍历添加price
        //Set<Long> priceSet = skus.stream ().map ( Sku::getPrice ).collect ( Collectors.toSet () );



        //提取公共属性 (获取的是json格式所以)反序列化
        List<Map<String,Object>> genericSpecs = mapper.readValue ( spuDetail.getSpecifications (),new TypeReference<List<Map<String,Object>>> (){});
        //提取特有属性
        Map<String,Object> specialMap = mapper.readValue ( spuDetail.getSpecTemplate (), new TypeReference<Map<String, Object>> () {
        } );


        //过滤规格，把所有可搜索的信息保存在Map中
        Map<String,Object> specifitionMap = new HashMap<> (  );

        //查询规格的key
        String searchable = "searchable";
        String v = "v";
        String k = "k";
        String options = "option";

        genericSpecs.forEach ( m ->{
           List<Map<String,Object>> params = (List<Map<String,Object>>) m.get ( "params" );
           params.forEach ( spe ->{
                if((boolean)spe.get ( searchable )){
                    if(spe.get ( v ) != null){
                        specifitionMap.put ( spe.get ( k ).toString (),spe.get ( v ) );
                    } else if (spe.get ( options ) != null) {
                        specifitionMap.put ( spe.get ( k ).toString (),spe.get ( options ) );
                    }
                }
           } );
        } );


        //搜索all的字段(拼接)
        String all = spu.getTitle () + StringUtils.join ( categoryNames, " " ) +" "+brand.getName ();


        Goods goods = new Goods ();
        goods.setBrandId ( spu.getBrandId () );
        goods.setCid1 ( spu.getCid1 () );
        goods.setCid2 ( spu.getCid2 () );
        goods.setCid3 ( spu.getCid3 () );
        goods.setCreateTime ( spu.getCreateTime () );
        goods.setId ( spu.getId () );
        goods.setAll ( all ); // 搜索字段包含标题分类品牌规格等
        goods.setPrice ( priceList ); // 所有的sku的price集合
        goods.setSkus ( mapper.writeValueAsString ( newSkus ) ); // 所有spu下的sku集合的json格式
        goods.setSpecs ( specifitionMap ); //所有可搜索的规格字段
        goods.setSubTitle ( spu.getSubTitle () );
        return goods;
    }


    /**
     * 门户搜索
     *
     * @param searchRequest
     * @return
     */
    public SearchResult search(SearchRequest searchRequest) {
        String key = searchRequest.getKey ();
        // 判断是否有搜索条件，如果没有，直接返回null。不允许搜索全部商品
        if (StringUtils.isBlank ( key )) {
            return null;
        }

        int page = searchRequest.getPage (); //ElasticSearch 页面起始从0开始，所以要-1
        Integer size = searchRequest.getSize ();
        //创建查询构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder ();
        //搜索结果过滤（_source["Field" ,"Field" ,"Field"]）
        queryBuilder.withSourceFilter ( new FetchSourceFilter ( new String[]{"id", "subTitle", "skus"}, null ) );
        //分页
        queryBuilder.withPageable ( PageRequest.of ( page - 1, size ) );
        //过滤
        queryBuilder.withQuery ( QueryBuilders.matchQuery ( "all", key ).operator ( Operator.AND ) );
        //添加分类和品牌的聚合
        String cagetegoryAggName = "categories";
        String brandAggName = "brands";
        queryBuilder.addAggregation ( AggregationBuilders.terms ( cagetegoryAggName ).field ( "cid3" ) );
        queryBuilder.addAggregation ( AggregationBuilders.terms ( brandAggName ).field ( "brandId" ) );
        //查询 (返回分页结果)
        AggregatedPage<Goods> result = (AggregatedPage<Goods>) goodsRepository.search ( queryBuilder.build () );
        //解析结果
        Long total = result.getTotalElements ();//总条数
        int totalPages = result.getTotalPages ();//总页数
        Long totalPages1 = Long.valueOf ( totalPages );
        List<Goods> goodsList = result.getContent (); //当前页结果
        //获取聚合结果集并解析 （封装一个方法）
        List<Map<String, Object>> categories = getCategoryAggResult ( result.getAggregation ( cagetegoryAggName ) );
        List<Brand> brands = getBrandAggResult ( result.getAggregation ( brandAggName ) );

        //  return new SearchResult ( total,totalPages1,goodsList,null,null );
        return new SearchResult ( total, totalPages1, goodsList, categories, brands );

    }



    //解析商品品牌聚合结果集
    private List<Brand> getBrandAggResult(Aggregation aggregation) {

       LongTerms terms = (LongTerms)aggregation;

     /*  //存放品牌信息们
        List<Brand> brands = new ArrayList<> (  );

        //获取聚合的桶
        terms.getBuckets ().forEach ( bucket -> {
            //根据品牌id查询品牌
            Brand brand = this.brandClient.queryBrandByBid ( bucket.getKeyAsNumber ().longValue () );
            //放入brand集合
            brands.add ( brand );
        } );

        return brands;*/

        //也可以用stream表达式
        return terms.getBuckets ().stream ().map ( bucket -> {
            return this.brandClient.queryBrandByBid ( bucket.getKeyAsNumber ().longValue () );
        } ).collect ( Collectors.toList () );
    }

    //解析商品种类聚合结果集
    public List<Map<String, Object>> getCategoryAggResult(Aggregation aggregation) {
        //还不知道为啥强制转型
        LongTerms terms = (LongTerms)aggregation;


        //获取桶,转化成list<map<String,Object>>
     return  terms.getBuckets ().stream().map ( bucket -> {
            //初始化map
            Map<String,Object> map = new HashMap<> (  );
            //获取桶中的分类ID（分类id就是聚合桶中的key）
            long id = bucket.getKeyAsNumber ().longValue ();
            //根据三级分类id查询分类名称
            List<Category> cateNames = this.categoryClient.queryCategoryByIds ( Arrays.asList ( id ) );
            map.put ( "id",id );
            map.put ( "name",cateNames.get ( 0 ) );
            return map;

        } ).collect( Collectors.toList () );


    }


    public void save(Long spuId)throws Exception {
        //构建spu对象
        Spu spu = this.goodsClient.querySpuBySpuId ( spuId );

            Goods goods = this.buildGoods ( spu );
            this.goodsRepository.save ( goods );

    }

    //服务mq消息中间件的删除方法
    public void delete(Long spuId) {
        //获取goods
        this.goodsRepository.deleteById ( spuId );
    }
}
