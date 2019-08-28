package com.cheer.huangou.item.service.impl;
import com.cheer.huangou.item.mapper.SpecificationMapper;
import com.cheer.huangou.item.service.SpecificationService;
import com.cheer.huangou.model.SpecParam;
import com.cheer.huangou.model.Specification;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author zhangjie
 * @title:
 * @data
 */
@Service
public class SpecificationServiceImpl implements SpecificationService {
    @Autowired
     private SpecificationMapper specificationMapper;


    @Override
    public Specification queryByCategoryId(Long cid) {
        return specificationMapper.selectByPrimaryKey ( cid );
    }

    //新增商品规格
    @Override
    public void insertSpecification(Specification specification) {
        this.specificationMapper.insertSelective ( specification );
    }

    //修改商品规格
    @Override
    public void updateSpecification(Specification specification) {
        this.specificationMapper.updateByPrimaryKeySelective ( specification );
    }


}
