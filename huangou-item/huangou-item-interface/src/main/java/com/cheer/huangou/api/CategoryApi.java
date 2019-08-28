package com.cheer.huangou.api;

import com.cheer.huangou.model.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author zhangjie
 * @title:
 * @data
 */

@RequestMapping("category")
public interface CategoryApi {
    /**
     * 根据品牌id找到对应分类
     * @param ids
     * @return
     */
    @GetMapping("list/ids")  //使用这个服务的这个方法
    List<Category> queryCategoryByIds(@RequestParam("ids") List<Long> ids );

    /**
     * 根据三级分类id集合查询分类名字们
     * @param ids
     * @return
     */
    @GetMapping("names")
    List<String> queryCategoryNameByIds(@RequestParam("ids") List<Long> ids);
}
