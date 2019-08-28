package com.cheer.huangou.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zhangjie
 * @title:
 * @data
 */
@RequestMapping("spec")
public interface SpecificationApi {
    /**
     * 根据商品三级种类id查询规格参数信息
     * @param id
     * @return
     */
    @GetMapping("spec/{id}")
    String querySpecificationByCategoryId(@PathVariable("id") Long id);
}
