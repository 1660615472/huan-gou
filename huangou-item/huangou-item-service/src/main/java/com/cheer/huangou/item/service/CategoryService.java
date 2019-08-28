package com.cheer.huangou.item.service;

import com.cheer.huangou.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> queryListByParent(Long parentId);

    //根据brandId删除中间表信息
    void deleteCategoryByBrandId(Long brandId);

   List<String> queryNameByIds(List<Long> ids);

    List<Category> queryByIds(List<Long> ids);
}
