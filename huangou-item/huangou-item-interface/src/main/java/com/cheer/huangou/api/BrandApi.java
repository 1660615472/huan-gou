package com.cheer.huangou.api;

import com.cheer.huangou.model.Brand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author zhangjie
 * @title:
 * @data
 */

@RequestMapping("brand")
public interface BrandApi {
    /**
     * 根据品牌id查询品牌
     * @param bid
     * @return
     */
    @GetMapping("{bid}")
   Brand queryBrandByBid(@PathVariable("bid") Long bid);
}
