package com.cheer.huangou.item.service.impl;

import com.cheer.huangou.item.mapper.CategoryMapper;
import com.cheer.huangou.item.service.CategoryService;
import com.cheer.huangou.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhangjie
 * @title:
 * @data
 */

@Service
public class CategoryServiceImpl implements CategoryService {
    @Resource
    private CategoryMapper categoryMapper;

    public List<Category> queryListByParent(Long parentId) {
        Category category = new Category ();
        category.setParentId ( parentId );

        List<Category> categoryList = this.categoryMapper.select ( category );
        return categoryList;
    }


    //根据brandId删除中间表信息
    @Override
    public void deleteCategoryByBrandId(Long brandId) {
        categoryMapper.deleteCategoryByBrandId ( brandId );
    }

    /**
     * 根据商品的三级目录查到商品分类名称
     *
     * @param ids
     * @return
     */
    @Override
    public List<String> queryNameByIds(List<Long> ids) {
        return this.categoryMapper.selectByIdList ( ids ).stream ().map ( Category::getName ).collect ( Collectors.toList () );

    }

    /**
     * 根据商品分类id集合查询商品分类信息
     * @param ids
     */
    @Override
    public List<Category> queryByIds(List<Long> ids) {
        List<Category> categories = this.categoryMapper.selectByIdList ( ids );
        return categories;
    }
}
