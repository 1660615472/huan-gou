package com.cheer.huangou.item.service.impl;

import com.cheer.huangou.common.vo.PageResult;
import com.cheer.huangou.item.mapper.*;
import com.cheer.huangou.item.service.BrandService;
import com.cheer.huangou.item.service.CategoryService;
import com.cheer.huangou.item.service.GoodsService;
import com.cheer.huangou.model.*;
import com.cheer.huangou.parameter.pojo.SpuQueryByPageParameter;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Select;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhangjie
 * @title:
 * @data
 */
@Log4j2
@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    //注入amqp模板（消息中间件）
    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 根据Spu查询分页信息
     *
     * @param //         page     第几页
     * @param //rows     每页多少行
     * @param //saleable 是否上架
     * @param //key      查询条件
     * @return
     */
    @Override
    public PageResult<SpuBo> querySpuByPageAndSort(SpuQueryByPageParameter spuQueryByPageParameter) {
        //查询SPU
        //分页。最多允许查询100条,使用分页插件
        PageHelper.startPage ( spuQueryByPageParameter.getPage (), Math.min ( spuQueryByPageParameter.getRows (), 100 ) );

        //创建查询条件
        Example example = new Example ( Spu.class );
        Example.Criteria criteria = example.createCriteria ();

        //条件过滤
        //是否过滤上下架
        if (spuQueryByPageParameter.getSaleable () != null) {
            System.out.println ( spuQueryByPageParameter.getSaleable () );
            criteria.orEqualTo ( "saleable", spuQueryByPageParameter.getSaleable () );
        }

        //是否模糊查询
        if (StringUtils.isNotBlank ( spuQueryByPageParameter.getKey () )) {
            criteria.andLike ( "title", "%" + spuQueryByPageParameter.getKey () + "%" );
        }

        //是否排序
        if (StringUtils.isNotBlank ( spuQueryByPageParameter.getSortBy () )) {
            example.setOrderByClause ( spuQueryByPageParameter.getSortBy () + (spuQueryByPageParameter.getDesc () ? " DESC" : " ASC") );
        }

        //过滤valid条件
        criteria.andEqualTo ( "valid", true );

        //根据拼接的sql语句查询页面
        Page<Spu> pageInfo = (Page<Spu>) this.goodsMapper.selectByExample ( example );

        List<SpuBo> list = pageInfo.getResult ().stream ().map ( spu -> {
            //把spu变为spuBo
            SpuBo spuBo = new SpuBo ();
                //属性拷贝
                BeanUtils.copyProperties ( spu, spuBo );



            //查询spu的商品分类名称，要查三级分类
            List<String> names = this.categoryService.queryNameByIds ( Arrays.asList ( spu.getCid1 (), spu.getCid2 (), spu.getCid3 () ) );

            //将分类名字拼接后存入
            spuBo.setCname ( StringUtils.join ( names, "/" ) );

            //查询spu的品牌名称
            Brand brand = this.brandMapper.selectByPrimaryKey ( spu.getBrandId () );
            spuBo.setBname ( brand.getName () );
            return spuBo;
        } ).collect ( Collectors.toList () );

        return new PageResult<> ( pageInfo.getTotal (), list );
    }


    /**
     * 新增商品
     *
     * @param spuBo
     */
    @Transactional //声明式事务 交给spring容器管理事务 遇到异常直接回滚，数据不被保存再数据库中
    @Override
    public void saveGoods(SpuBo spuBo) {
        //先保存spu信息
        spuBo.setSaleable ( true ); //商品默认上架状态
        spuBo.setValid ( true ); //是否删除 默认false
        spuBo.setCreateTime ( new Date () );//新建商品时间
        spuBo.setLastUpdateTime ( spuBo.getCreateTime () );//最后修改商品信息时间
        //先插入SPU信息
        this.spuMapper.insert ( spuBo );


        //保存spu详情
        SpuDetail spuDetail = spuBo.getSpuDetail();
        spuDetail.setSpuId ( spuBo.getId () );
        System.out.println(spuDetail.getSpecifications().length());
        //插入detail信息
        this.spuDetailMapper.insertSelective ( spuDetail );



        //新增Skus和库存信息
        saveSkuAndStock ( spuBo.getSkus (), spuBo.getId () );

        //给交换机发消息
        sendMessage("insert",spuBo.getId ());

    }

    /**
     *
     * @param skus 新增的skus
     * @param spuId sku所属的spuId
     */
    //批量新增sku和库存信息的方法
    private void saveSkuAndStock(List<Sku> skus, Long spuId) {
        for (Sku sku : skus) {
            if (!sku.getEnable ()) {//如果用户新增的商品选择无效的话用contine跳过当前新增商品事务，执行下一个循环
                continue;
            }
            //保存sku
            sku.setSpuId ( spuId );
            //初始化时间
            sku.setCreateTime ( new Date () );
            sku.setLastUpdateTime ( sku.getCreateTime () );
            //插入sku数据库
            this.skuMapper.insert ( sku );

            //保存库存信息
            Stock stock = new Stock ();
            stock.setSkuId ( sku.getId () );
            stock.setStock ( sku.getStock () );
            //插入库存表
            this.stockMapper.insert ( stock );

        }

    }

    /**
     * 给消息中间件交换机发消息的方法
     * @param id
     * @Param type 消息类型自定义
     */
    public void sendMessage(String type,Long id){
        //给交换机发消息
        try {//捕捉发送消息可能出现的异常
            this.amqpTemplate.convertAndSend ( "item."+type,id );
        } catch (AmqpException e) {
            e.printStackTrace ();
        }

    }

    /**
     * 根据spuid查询SpuDetail
     * @param id
     * @return
     */
    @Override
    public SpuDetail querySpuDetailById(Long id) {
     return this.spuDetailMapper.selectByPrimaryKey ( id );
    }

    /**
     * 根据spuid查询sku集合
     * @param id
     * @return
     */
    @Override
    public List<Sku> querySkusBySpuId(Long id) {
        Sku sku = new Sku ();
        sku.setSpuId ( id );
        return this.skuMapper.select ( sku );
    }

    /**
     * 根据id查询商品信息
     * @param id
     * @return
     */
    @Override
    public SpuBo queryGoodsById(Long id) {
        /**
         * 第一页所需信息如下：
         * 1.商品的分类信息、所属品牌、商品标题、商品卖点（子标题）
         * 2.商品的包装清单、售后服务
         */
        Spu spu=this.spuMapper.selectByPrimaryKey(id);
        SpuDetail spuDetail = this.spuDetailMapper.selectByPrimaryKey(spu.getId());

        Example example = new Example(Sku.class);
        example.createCriteria().andEqualTo("spuId",spu.getId());
        List<Sku> skuList = this.skuMapper.selectByExample(example);
        List<Long> skuIdList = new ArrayList<> ();
        for (Sku sku : skuList){
            skuIdList.add(sku.getId());
        }

        List<Stock> stocks = this.stockMapper.selectByIdList(skuIdList);

        for (Sku sku:skuList){
            for (Stock stock : stocks){
                if (sku.getId().equals(stock.getSkuId())){
                    sku.setStock(stock.getStock());
                }
            }
        }

        SpuBo spuBo = new SpuBo(spu.getBrandId(),spu.getCid1(),spu.getCid2(),spu.getCid3(),spu.getTitle(),
                spu.getSubTitle(),spu.getSaleable(),spu.getValid(),spu.getCreateTime(),spu.getLastUpdateTime());
        spuBo.setSpuDetail(spuDetail);
        spuBo.setSkus(skuList);
        return spuBo;
    }


    /**
     * 更新商品信息
     * @param spuBo
     */
    @Transactional
    @Override
    public void updateGoods(SpuBo spuBo) {
        //查询修改前的sku信息
        List<Sku> skus = this.querySkusBySpuId ( spuBo.getId () );
        //如果以前的sku有信息就删除
        if(!CollectionUtils.isEmpty ( skus )){
            //找到所有的skuId进行删除
            //Stream() 把一个数据 可以是集合数组 I/O 转化成流
            //map()用于映射每个元素对应的结果
          List<Long>  ids = skus.stream ().map ( s -> s.getId () ).collect ( Collectors.toList () );

          //删除以前的库存 (Example 相当于where后面的部分sql语句)
            Example example = new Example ( Stock.class );
            //example.createCriteria () 创建条件容器
            example.createCriteria ().andIn ( "skuId",ids );
            //根据skiId删除sku
            this.stockMapper.deleteByExample ( example );

            //删除以前的sku
            Sku oldSku = new Sku ();
            oldSku.setSpuId ( spuBo.getId () );
            this.skuMapper.delete ( oldSku );

            //给交换机发消息
            sendMessage("update",spuBo.getId ());

        }

        //新增sku库存
        saveSkuAndStock ( spuBo.getSkus (),spuBo.getId () );

        //跟新spu
        spuBo.setLastUpdateTime ( new Date (  ) );
        spuBo.setCreateTime ( null );
        spuBo.setValid ( null );
        spuBo.setSaleable ( null );
        this.spuMapper.updateByPrimaryKeySelective ( spuBo );

        //跟新spu详情     updateByPrimaryKeySelective() 该方法对我注入的字段全部更新
        this.spuDetailMapper.updateByPrimaryKeySelective ( spuBo.getSpuDetail () );
    }


    /**
     * 根据商品id（spuId）设置上下架
     * @param gid 商品id
     */
    @Override
    public void goodSoldOut(long gid) {
        //通过商品id查找商品信息
        Spu oldSpu = this.goodsMapper.selectByPrimaryKey ( gid );
        //设置sql语句，通过商品ID查找其下所有sku信息
        Example example = new Example ( Sku.class );
        //追加生成where后字句
        example.createCriteria ().andEqualTo ( "spuId",gid );
        //查询所有sku
        List<Sku> skus = this.skuMapper.selectByExample ( example );
        //如果原商品是上架状态就下架它

        oldSpu.setSaleable (! oldSpu.getSaleable () );
        //更新状态
        this.spuMapper.updateByPrimaryKeySelective ( oldSpu );
        //下架sku中的具体商品
        for (Sku sku : skus) {
            sku.setEnable ( false );
            //跟新下架后的sku信息
            skuMapper.updateByPrimaryKeySelective ( sku );
        }
    }


    /**
     * 逻辑删除，实际数据库不删除
     * @param spuId
     */
    @Override
    public void deleteBySpuId(String spuId) {
        String[] split = spuId.split ( "-" );
        //根据spuId找到spu信息
        for (String s : split) {
            Spu spu = this.spuMapper.selectByPrimaryKey ( Long.valueOf ( s ) );
            spu.setValid ( false );
            this.spuMapper.updateByPrimaryKeySelective ( spu );
        }
        SpuBo spuBo = this.goodsService.queryGoodsById ( Long.valueOf ( spuId ) );
        //给交换机发消息
        sendMessage("delete",spuBo.getId ());

    }


}
