package com.cheer.search.pojo;

import com.cheer.huangou.common.vo.PageResult;
import com.cheer.huangou.model.Brand;
import com.cheer.huangou.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author zhangjie
 * @title:扩展查询分页结果集的字段
 * @data
 */

@NoArgsConstructor
@Data
public class SearchResult extends PageResult<Goods> {
    private List<Map<String,Object>> categories;
    private List<Brand> brands;

    public SearchResult(Long total,Long totalPage, List items,List<Map<String,Object>> categories, List<Brand> brands){
        super ( total,totalPage,items );
        this.categories=categories;
        this.brands=brands;
    }
    public SearchResult (List<Map<String,Object>> categories,List<Brand> brands){
        this.categories =categories;
        this.brands=brands;
    }
}
