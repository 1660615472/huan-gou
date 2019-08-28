package com.cheer.huangou.goods.service;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.concurrent.ExecutionException;

/**
 * @author zhangjie
 * @title:页面静态化
 * @data
 */
@Log4j2
@Service
public class GoodsHtmlService {
    //注入模板引擎
    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private GoodsService goodsService;

    //针对spuid生成静态页面
    public void createHtml(Long spuId) throws  InterruptedException, ExecutionException {

        //初始化运行上下文
        Context context = new Context();
        //设置数据模板
        context.setVariables ( this.goodsService.loadModel ( spuId ) );
        //静态化存储位置
        File file = new File ( "D:\\MYJAVA\\nginx\\nginx-1.16.0\\html\\item\\" + spuId + ".html" );
        PrintWriter printWriter = null; //提升作用域
        try {
             printWriter = new PrintWriter ( file );
            //模板名称
            this.templateEngine.process ( "item", context,printWriter );
        }catch (FileNotFoundException e){
            log.error("页面静态化出错：{}"+e,spuId);
        }finally {
            if(printWriter!=null){
                printWriter.close ();
            }
        }

    }

    //删除静态页面的方法
    public void deleteHtml(Long spuId) {
        File file = new File ( "D:\\MYJAVA\\nginx\\nginx-1.16.0\\html\\item\\" + spuId + ".html" );
        //文件存在就删除
        file.deleteOnExit ();
    }
}
