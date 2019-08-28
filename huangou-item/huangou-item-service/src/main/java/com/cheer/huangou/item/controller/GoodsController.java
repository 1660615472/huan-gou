package com.cheer.huangou.item.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.cheer.huangou.common.vo.PageResult;
import com.cheer.huangou.item.mapper.SkuMapper;
import com.cheer.huangou.item.mapper.SpuDetailMapper;
import com.cheer.huangou.item.mapper.SpuMapper;
import com.cheer.huangou.item.mapper.StockMapper;
import com.cheer.huangou.item.service.GoodsService;
import com.cheer.huangou.model.*;
import com.cheer.huangou.parameter.pojo.SpuQueryByPageParameter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangjie
 * @title:
 * @data
 */
@Log4j2
@RequestMapping("goods")
@RestController
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    /**
     * 分页显示商品spu信息
     *
     * @param page
     * @param rows
     * @param key
     * @return
     */
    @GetMapping("/spu/page")
    public ResponseEntity<PageResult<SpuBo>> querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "saleable", defaultValue = "true") Boolean saleable

    ) {
        //分页查询spu信息
        SpuQueryByPageParameter spuQueryByPageParameter = new SpuQueryByPageParameter ( page, rows, sortBy, desc, key, saleable );
        PageResult<SpuBo> spuBoPageResult = this.goodsService.querySpuByPageAndSort ( spuQueryByPageParameter );
        if (spuBoPageResult == null || spuBoPageResult.getItems ().size () == 0) {
            return new ResponseEntity<> ( HttpStatus.NOT_FOUND );
        }
        return ResponseEntity.ok ( spuBoPageResult );
    }

    /**
     * 新增商品
     * 需要插入信息到几个表中
     *
     * @param spuBo
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> saveGoods(@RequestBody SpuBo spuBo) {
        try {
            this.goodsService.saveGoods ( spuBo );
            return new ResponseEntity<> ( HttpStatus.CREATED );
        } catch (Exception e) {
            e.printStackTrace ();
            return new ResponseEntity<> ( HttpStatus.INTERNAL_SERVER_ERROR );
        }
    }


    /**
     * 根据id查询商品
     *
     * @param id
     * @return
     */
    @GetMapping("/spu/{id}")
    public ResponseEntity<SpuBo> queryGoodsById(@PathVariable("id") Long id) {
        SpuBo spuBo = this.goodsService.queryGoodsById ( id );
        if (spuBo == null) {
            return ResponseEntity.status ( HttpStatus.NOT_FOUND ).build ();
        }
        return ResponseEntity.ok ( spuBo );
    }


    @PutMapping
    public ResponseEntity<Void> updateGoods(@RequestBody SpuBo spuBo) {
        try {
            this.goodsService.updateGoods ( spuBo );
            return new ResponseEntity<> ( HttpStatus.NO_CONTENT );
        } catch (Exception e) {
            e.printStackTrace ();
            return new ResponseEntity<> ( HttpStatus.INTERNAL_SERVER_ERROR );
        }
    }


    /**
     * 根据商品Id设置上下架
     *
     * @param ids spuId
     * @return
     */
    @PutMapping("/spu/out/{id}")
    public ResponseEntity<Void> goodSoldOut(@PathVariable("id") String ids) {
        String separator = "-"; //分离符字符串
        if (ids.contains ( separator )) {
            String[] goodsId = ids.split ( separator );//split方法 以"-"分割字符串
            for (String id : goodsId) {
                this.goodsService.goodSoldOut ( Long.parseLong ( id ) );//Long.parseLong 使用包装类的方法将字符串转换成long类型
            }
        } else {
            this.goodsService.goodSoldOut ( Long.parseLong ( ids ) );
            return new ResponseEntity ( HttpStatus.OK );
        }
        return new ResponseEntity<> ( HttpStatus.OK );
    }


    @DeleteMapping("/spu/{spuId}")
    public ResponseEntity<Void> deleteGoods(@PathVariable("spuId") String spuId) {
        this.goodsService.deleteBySpuId ( spuId ); //逻辑删除(假删除)
        return ResponseEntity.ok ().build ();
    }

    /**
     * 根据spuid查询spudetail
     *
     * @param spuId
     * @return
     */
    @GetMapping("/spu/detail/{id}")
    public ResponseEntity<SpuDetail> queryDetailById(@PathVariable("id") Long spuId) {
        return ResponseEntity.ok ( goodsService.querySpuDetailById ( spuId ) );
    }


    /**
     * 根据spuid查询下所有的sku
     *
     * @param spuId
     * @return
     */
    @GetMapping("sku/list")
    public ResponseEntity<List<Sku>> querySkuBySpuId(@RequestParam("id") Long spuId) {
        return ResponseEntity.ok ( goodsService.querySkusBySpuId ( spuId ) );
    }


    @GetMapping("{spuId}")
    public ResponseEntity<Spu> querySpuBySpuId(@PathVariable("spuId") Long spuId){
        Spu spu = this.spuMapper.selectByPrimaryKey ( spuId );
        if(spu==null){
            return new ResponseEntity<> ( HttpStatus.NOT_FOUND );
        }
        return ResponseEntity.ok ( spu );
    }



}
