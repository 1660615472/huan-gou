package com.cheer.huangou.goods.service;

import com.cheer.huangou.goods.client.BrandClient;
import com.cheer.huangou.goods.client.CategoryClient;
import com.cheer.huangou.goods.client.GoodsClient;
import com.cheer.huangou.goods.client.SpecificationClient;
import com.cheer.huangou.model.*;
import com.cheer.huangou.utils.JsonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zhangjie
 * @title:
 * @data
 */

@Service
public class GoodsService {

    @Autowired
    private BrandClient brandClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SpecificationClient specificationClient;


    //返回数据给GoodsController的model给前台渲染
    //封装成map对象
    public Map<String, Object> loadModel(Long spuId) throws InterruptedException, ExecutionException {
        //初始化map存放详情页信息
        Map<String, Object> map = new HashMap<> ();
        //获取spu对应的spudetail信息
        SpuDetail spuDetail = this.goodsClient.queryDetailById ( spuId );

     /*   final CountDownLatch countDownLatch = new CountDownLatch(2);
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        SpuBo spuBo = executorService.submit(() -> {
            countDownLatch.countDown();
            return this.goodsClient.queryGoodsById (spuId);
        }).get();
*/
      /*  SpuBo spuBo = this.goodsClient.queryGoodsById ( spuId );*/
        //获取spuid对应的spu
        Spu spu = this.goodsClient.querySpuBySpuId ( spuId );

        //获取品牌信息
        Brand brand = this.brandClient.queryBrandByBid ( spu.getBrandId () );
        //存储三级分类的map需要分类的id所以抽取出cid集合
        List<Long> cidList = Arrays.asList ( spu.getCid1 (), spu.getCid2 (), spu.getCid3 () );
        List<String> categoriesNames = this.categoryClient.queryCategoryNameByIds ( cidList );
        //获取三级分类信息: List<Map<String,Object>>
        //转成Map格式，初始化一个map
        List<Map<String, Object>> cateMap = new ArrayList<> ();
        for (int i = 0; i < cidList.size (); i++) {
            //初始化map
            Map<String, Object> map1 = new HashMap<> ();
            map1.put ( "id", cidList.get ( i ) );
            map1.put ( "name", categoriesNames.get ( i ) );
            cateMap.add ( map1 );

        }

        //查询spuid下对应的sku集合
        List<Sku> skus = this.goodsClient.querySkuBySpuId ( spuId );

        /**
         * 对于规格属性的处理需要注意以下几点：
         *      1. 所有规格都保存为id和name形式
         *      2. 规格对应的值保存为id和value形式
         *      3. 都是map形式
         *      4. 将特有规格参数单独抽取
         */

        //获取所有规格参数，然后封装成为id和name形式的数据
        String allSpecJson = spuDetail.getSpecifications ();
        //封装成List<Map<String,Object>>
        List<Map<String, Object>> allSpecs = JsonUtils.nativeRead ( allSpecJson, new TypeReference<List<Map<String, Object>>> () {
        } );
        //初始化存放规格名的map
        Map<Integer,String> specName = new HashMap<> (  );
        //初始化存方法规格值的map
        Map<Integer,Object> specValue = new HashMap<> (  );
        getAllSpecifications(allSpecs,specName,specValue);


        //获取特有的规格参数
        String specTJson = spuDetail.getSpecTemplate ();
        Map<String,String[]> specs = JsonUtils.nativeRead(specTJson, new TypeReference<Map<String, String[]>>() {
        });
        Map<Integer,String> specialParamName = new HashMap<>();
        Map<Integer,String[]> specialParamValue = new HashMap<>();
        //填充特有规格参数的map集合
        this.getSpecialSpec(specs,specName,specValue,specialParamName,specialParamValue);

        //按照组构造规格参数
        List<Map<String,Object>> groups = this.getGroupsSpec(allSpecs,specName,specValue);


        map.put ( "spu", spu );
        map.put ( "spuDetail", spuDetail );
        map.put ( "categories", cateMap );
        map.put ( "brand", brand );
        map.put ( "skus", skus );
        map.put ( "groups", groups );
        map.put("specName",specName);
        map.put("specValue",specValue);
        map.put("specialParamName",specialParamName);
        map.put("specialParamValue",specialParamValue);

        return map;
    }


    //填充上面2个map的方法，把规格参数名与规格参数值分别填充到2个map中
    private void getAllSpecifications(List<Map<String, Object>> allSpecs, Map<Integer, String> specName, Map<Integer, Object> specValue) {
        String k = "k";
        String v = "v";
        String unit = "unit";
        String numerical = "numerical";
        String options ="options";
        int i = 0;
        if (allSpecs != null){
            for (Map<String,Object> s : allSpecs){
                List<Map<String, Object>> params = (List<Map<String, Object>>) s.get("params");
                for (Map<String,Object> param :params){
                    String result;
                    if (param.get(v) == null){
                        result = "无";
                    }else{
                        result = param.get(v).toString();
                    }
                    if (param.containsKey(numerical) && (boolean) param.get(numerical)) {
                        if (result.contains(".")){
                            Double d = Double.valueOf(result);
                            if (d.intValue() == d){
                                result = d.intValue()+"";
                            }
                        }
                        i++;
                        specName.put(i,param.get(k).toString());
                        specValue.put(i,result+param.get(unit).toString());
                    } else if (param.containsKey(options)){
                        i++;
                        specName.put(i,param.get(k).toString());
                        specValue.put(i,param.get(options));
                    }else {
                        i++;
                        specName.put(i,param.get(k).toString());
                        specValue.put(i,param.get(v));
                    }
                }
            }
        }
    }

    //填充特有规格参数的map集合的方法
    private void getSpecialSpec(Map<String, String[]> specs, Map<Integer, String> specName, Map<Integer, Object> specValue, Map<Integer, String> specialParamName, Map<Integer, String[]> specialParamValue) {
        if (specs != null) {
            for (Map.Entry<String, String[]> entry : specs.entrySet()) {
                String key = entry.getKey();
                for (Map.Entry<Integer,String> e : specName.entrySet()) {
                    if (e.getValue().equals(key)){
                        specialParamName.put(e.getKey(),e.getValue());
                        //因为是放在数组里面，所以要先去除两个方括号，然后再以逗号分割成数组
                        String  s = specValue.get(e.getKey()).toString();
                        String result = StringUtils.substring(s,1,s.length()-1);
                        specialParamValue.put(e.getKey(), result.split(","));
                    }
                }
            }
        }
    }

    private List<Map<String, Object>> getGroupsSpec(List<Map<String, Object>> allSpecs, Map<Integer, String> specName, Map<Integer, Object> specValue) {
        List<Map<String, Object>> groups = new ArrayList<>();
        int i = 0;
        int j = 0;
        for (Map<String,Object> spec :allSpecs){
            List<Map<String, Object>> params = (List<Map<String, Object>>) spec.get("params");
            List<Map<String,Object>> temp = new ArrayList<>();
            for (Map<String,Object> param :params) {
                for (Map.Entry<Integer, String> entry : specName.entrySet()) {
                    if (entry.getValue().equals(param.get("k").toString())) {
                        String value = specValue.get(entry.getKey()) != null ? specValue.get(entry.getKey()).toString() : "无";
                        Map<String, Object> temp3 = new HashMap<>(16);
                        temp3.put("id", ++j);
                        temp3.put("name", entry.getValue());
                        temp3.put("value", value);
                        temp.add(temp3);
                    }
                }
            }
            Map<String,Object> temp2 = new HashMap<>(16);
            temp2.put("params",temp);
            temp2.put("id",++i);
            temp2.put("name",spec.get("group"));
            groups.add(temp2);
        }
        return groups;
    }
}
