package com.cheer.huangou.item.controller;

import com.cheer.huangou.item.service.BrandService;
import com.cheer.huangou.item.service.CategoryService;
import com.cheer.huangou.model.Category;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author zhangjie
 * @title:
 * @data
 */

@RequestMapping("category")
@Log4j2
@RestController
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandService brandService;


    @GetMapping("list")
    public ResponseEntity<List<Category>> list(@RequestParam(value = "pid",defaultValue = "0") Long parentId){
        log.traceEntry();
        List<Category> categoryList = this.categoryService.queryListByParent(parentId);
        return CollectionUtils.isEmpty(categoryList) ? new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                ResponseEntity.ok(categoryList);

    }


    //修改商品信息（根据商品id查找种类id）
    @GetMapping("bid/{bid}")
    public ResponseEntity<List<Category>> queryByBrandId(@PathVariable(value="bid",required=false) Long bid){
        List<Category> categories = this.brandService.queryByBrandId ( bid );
        if(categories == null || categories.size ()<1){
            return new ResponseEntity<> ( HttpStatus.BAD_REQUEST );
        }
        return ResponseEntity.ok ( categories );
    }

    //根据商品分类id查询商品分类
    @GetMapping("list/ids")
    public ResponseEntity<List<Category>> queryCategoryByIds(@RequestParam("ids") List<Long> ids ){
        List<Category> categories = this.categoryService.queryByIds ( ids );
        return ResponseEntity.ok ( categories );
    }

    //根据spuid查询三级分类名字（String）
    @GetMapping("names")
    public ResponseEntity<List<String>> queryCategoryNameByIds(@RequestParam("ids") List<Long> ids){
        List<String> name = this.categoryService.queryNameByIds ( ids );
        if(name == null){
            return new ResponseEntity<> ( HttpStatus.NOT_FOUND );
        }
        return ResponseEntity.ok ( name);
    }
}
