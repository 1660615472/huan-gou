package com.cheer.huangou.item.service;

import com.cheer.huangou.common.vo.PageResult;
import com.cheer.huangou.model.Brand;
import com.cheer.huangou.model.Category;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author zhangjie
 * @title:
 * @data
 */

public interface BrandService {

    //页面查询方法
    PageResult<Brand> queryBrandByPageAndSort(Integer page, Integer rows, String sortBy, Boolean desc, String key);

    //添加商品信息方法
    void saveBrand(Brand brand, List<Long> cids);

    //根据品牌id查询商品分类信息
    public List<Category> queryByBrandId(Long bid);

    //修改商品信息方法
     void updateBread(Brand brand,List<Long> cids);

     //根据品牌id删除商品表对应的商品信息和中间表对应的商品信息
    void deleteBrand(Long brandId);

    //根据种类id查询商品（商品集合）
    List<Brand> queryBrandByCid(Long cid);

    //根据品牌id查询品牌
    Brand queryBrandIdbyBid(Long bid);
}
