package com.cheer.search.controller;

import com.cheer.huangou.common.vo.PageResult;
import com.cheer.search.pojo.Goods;
import com.cheer.search.pojo.SearchRequest;
import com.cheer.search.pojo.SearchResult;
import com.cheer.search.service.impl.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangjie
 * @title:
 * @data
 */

@RestController
public class SearchController {
    @Autowired
    private SearchService searchService;

    /**
     * 门户搜索功能
     * @param searchRequest
     * @return
     */
    @PostMapping("page")
    public ResponseEntity<SearchResult> search(@RequestBody SearchRequest searchRequest){
        SearchResult result = searchService.search ( searchRequest );
        if (result == null) {
            return new ResponseEntity<>( HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok ( result );
    }
}
