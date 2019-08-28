package com.cheer.huangou.item.service;

import com.cheer.huangou.model.Specification;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhangjie
 * @title:产品规格实现类
 * @data
 */
public interface SpecificationService {
    //根据种类id查询商品规格
    Specification queryByCategoryId(Long cid);

    //新增商品规格
    void insertSpecification(Specification specification);

    //跟新商品规格
    void updateSpecification(Specification specification);
}
