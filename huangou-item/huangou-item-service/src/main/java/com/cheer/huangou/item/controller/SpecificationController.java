package com.cheer.huangou.item.controller;

import com.cheer.huangou.item.mapper.SpecificationMapper;
import com.cheer.huangou.item.service.SpecificationService;
import com.cheer.huangou.model.Category;
import com.cheer.huangou.model.Specification;
import lombok.extern.log4j.Log4j2;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author zhangjie
 * @title:
 * @data
 */
@Log4j2
@RequestMapping("spec")
@RestController
public class SpecificationController {
    @Autowired
    SpecificationService specificationService;

    @Autowired
    SpecificationMapper specificationMapper;

    /**
     * 根据商品三级种类id查询规格参数信息
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public ResponseEntity<String> querySpecificationByCategoryId(@PathVariable("id") Long id){
        Specification spec = this.specificationService.queryByCategoryId ( id );
        if(spec ==null ){
            return new ResponseEntity<> ( HttpStatus.NOT_FOUND );
        }
        return ResponseEntity.ok (spec.getSpecifications ());
    }

    /**
     * 新增商品规格
     * @param specification
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> insertSpecification(Specification specification){
        this.specificationService.insertSpecification ( specification );
        return ResponseEntity.ok ().build ();
    }

    /**
     * 修改规格方法
     * @param specification
     * @return
     */
  @PutMapping
    public ResponseEntity<Void> updateSpecification(Specification specification){
        this.specificationService.updateSpecification ( specification );
        return ResponseEntity.ok (  ).build ();
  }

  @DeleteMapping("{cid}")
    public ResponseEntity<Void> deleteSpecification(@PathVariable("cid") Long cid){
        this.specificationMapper.deleteByPrimaryKey ( cid );
        return ResponseEntity.ok (  ).build ();
  }

/*  @GetMapping("parms")
    public ResponseEntity<List<SpecParam>> queryParamList(@RequestParam(value = "gid",required = false) Long gid,
                                                          @RequestParam(value = "cid",required = false)Long cid,
                                                          @RequestParam(value = "searching",required = false)Boolean searching){
      return ResponseEntity.ok ( specificationService.queryParamList ( gid,cid,searching ) );
  }*/


}
