package com.cheer.huangou.goods.controller;

import com.cheer.huangou.goods.service.GoodsHtmlService;
import com.cheer.huangou.goods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author zhangjie
 * @title:
 * @data
 */

@Controller
@RequestMapping("item")
public class  GoodsController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsHtmlService goodsHtmlService;

    /**
     * 跳转到商品详情页
     *
     * @param model
     * @param id
     * @return
     */
    @GetMapping("{id}.html")
    public String toItemPage(@PathVariable("id") Long id, Model model)throws Exception { //需要传递数据到页面所以要用model
        Map<String, Object> map = this.goodsService.loadModel ( id );
        model.addAllAttributes ( map );
        //生成静态页面
        this.goodsHtmlService.createHtml ( id );
        return "item";
    }
}
