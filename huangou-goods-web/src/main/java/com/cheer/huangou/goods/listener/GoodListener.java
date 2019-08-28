package com.cheer.huangou.goods.listener;

import com.cheer.huangou.goods.service.GoodsHtmlService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

/**
 * @author zhangjie
 * @title:消费者监听器
 * @data
 */

@Component //仅表示一个组件 交给spring容器管理
public class GoodListener {

    @Autowired
    private GoodsHtmlService goodsHtmlService;

    /**
     * @param id
     */                                              //value：定义队列名字  declare:是否持久化
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "huangou.item.save.queue", declare = "true"),
            //定义交换机名称   ignoreDeclarationExceptions：是否忽略声明异常                              TOPIC类型
            exchange = @Exchange(value = "huangou.item.exchange", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),
            //
            key = {"item.insert", "item.update"}))
    public void save(Long id) throws Exception {
        //判断消息是否为null
        if (id == null) {
            return;
        }
        //
        this.goodsHtmlService.createHtml ( id );

    }


    /**
     * @param id
     */                                              //value：定义队列名字  declare:是否持久化
    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "item.delete.queue", declare = "true"),
            //定义交换机名称   ignoreDeclarationExceptions：是否忽略声明异常                              TOPIC类型
            exchange = @Exchange(value = "huangou.item.exchange", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),
            //
            key = {"item.delete"}))
    public void delete(Long id) {
        //判断消息是否为null
        if (id == null) {
            return;
        }
        this.goodsHtmlService.deleteHtml ( id );
    }
}
