package com.cheer.huangou.item.service;

import com.cheer.huangou.common.vo.PageResult;
import com.cheer.huangou.item.mapper.GoodsMapper;
import com.cheer.huangou.model.Sku;
import com.cheer.huangou.model.Spu;
import com.cheer.huangou.model.SpuBo;
import com.cheer.huangou.model.SpuDetail;
import com.cheer.huangou.parameter.pojo.SpuQueryByPageParameter;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author zhangjie
 * @title:
 * @data
 */
public interface GoodsService {
    PageResult<SpuBo> querySpuByPageAndSort(SpuQueryByPageParameter spuQueryByPageParameter);

    //新增商品
    void saveGoods(SpuBo spuBo);

    //根据id查询spuDetail
    SpuDetail querySpuDetailById(Long id);

    List<Sku> querySkusBySpuId(Long id);


    SpuBo queryGoodsById(Long id);

    void updateGoods(SpuBo spuBo);

    void goodSoldOut(long gid);

    //逻辑删除商品信息（假删除）
    void deleteBySpuId(String spuId);
}
