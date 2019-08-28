package com.cheer.huangou.item.mapper;

import com.cheer.huangou.model.Category;
import org.apache.ibatis.annotations.Delete;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author zhangjie
 * @title:
 * @data
 */


public interface CategoryMapper extends Mapper<Category>,SelectByIdListMapper<Category, Long> {
    //根据brandId删除中间表信息
    @Delete ( "delete from tb_category_brand where brand_id = #{brandId}" )
    void deleteCategoryByBrandId(Long brandId);
}
