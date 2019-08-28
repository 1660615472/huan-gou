package com.cheer.huangou.item.controller;

import com.cheer.huangou.common.vo.PageResult;
import com.cheer.huangou.item.service.BrandService;
import com.cheer.huangou.item.service.CategoryService;
import com.cheer.huangou.model.Brand;
import com.cheer.huangou.model.Category;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RequestMapping("brand")
@RestController
public class BrandController {
    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 分页查询品牌
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @param key
     * @return
     */

    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> queryBrandByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
            @RequestParam(value = "key", required = false) String key) {
        log.traceEntry();

        PageResult<Brand> pageResult = this.brandService.queryBrandByPageAndSort(page, rows, sortBy, desc, key);

        if (null == pageResult || pageResult.getItems().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(pageResult);
    }


    /**
     * 新增品牌
     * @param brand
     * @param cids 商品类别列表
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> saveBrand(Brand brand, @RequestParam("categories") List<Long> cids) {
        log.traceEntry ();

        this.brandService.saveBrand ( brand, cids );
        return new ResponseEntity<> ( HttpStatus.CREATED );
    }

    /**
     * 修改商品信息方法
     *
     * @param brand
     * @param cids
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> updateBrand(Brand brand, @RequestParam("categories") List<Long> cids) {
       this.brandService.updateBread ( brand, cids );

        return ResponseEntity.status ( HttpStatus.ACCEPTED ).build ();

    }

    /**
     * 根据品牌id删除品牌
     * 需要删除品牌表和中间表中对应的品牌信息
     * @param brandId
     * @return
     */
    @DeleteMapping("bid/{bid}")
    public ResponseEntity<Void> deleteBrand(@PathVariable("bid") Long brandId){
        //删除数据前先删除商品表信息
        this.brandService.deleteBrand ( brandId );

        //然后删除中间表对应的商品信息
        this.categoryService.deleteCategoryByBrandId ( brandId );

        return ResponseEntity.status ( HttpStatus.OK ).build ();

    }


    /**
     * 根据种类id查询品牌信息集合
     * @param cid
     * @return
     */
    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandByCid(@PathVariable("cid") Long cid){
       return ResponseEntity.ok ( this.brandService.queryBrandByCid(cid) );
    }

    /**
     * 根据品牌id查询品牌
     * @param bid
     * @return
     */
    @GetMapping("{bid}")
    public ResponseEntity<Brand> queryBrandByBid(@PathVariable("bid") Long bid){
      return ResponseEntity.ok ( this.brandService.queryBrandIdbyBid ( bid ) );
    }

}


