package com.cheer.huangou.api;

import com.cheer.huangou.common.vo.PageResult;
import com.cheer.huangou.model.Sku;
import com.cheer.huangou.model.Spu;
import com.cheer.huangou.model.SpuBo;
import com.cheer.huangou.model.SpuDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author zhangjie
 * @title:
 * @data
 */

@RequestMapping("goods")
public interface GoodsApi {
    /**
     * 根据spuid查询spudetail
     *
     * @param spuId
     * @return
     */
    @GetMapping("/spu/detail/{id}")
   SpuDetail queryDetailById(@PathVariable("id") Long spuId);


    /**
     * 根据spuid查询下所有的sku
     *
     * @param spuId
     * @return
     */
    @GetMapping("sku/list")
    List<Sku> querySkuBySpuId(@RequestParam("id") Long spuId);


    /**
     *根据spuid查询spu信息
     * @param spuId
     * @return
     */
    @GetMapping("{spuId}")
    Spu querySpuBySpuId(@PathVariable("spuId") Long spuId);


    @GetMapping("/spu/spuId")
    SpuBo queryGoodsById(@PathVariable("spuId") Long spuId);

    /**
     * 分页显示商品spu信息
     *
     * @param page
     * @param rows
     * @param key
     * @return
     */
    @GetMapping("/spu/page")
    PageResult<SpuBo> querySpuByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
            @RequestParam(value = "key", required = false) String key,
            @RequestParam(value = "saleable", defaultValue = "true") Boolean saleable

    );
}
