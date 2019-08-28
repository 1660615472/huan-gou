package com.cheer.search.listener;

import com.cheer.search.service.impl.SearchService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zhangjie
 * @title:
 * @data
 */
@Component
public class GoodsListener {
    @Autowired
    private SearchService searchService;

    @RabbitListener(bindings = @QueueBinding ( value = @Queue(value = "huangou.search.save.queue",durable = "true"),
                    //交换机名称要和生产者交换机名字一样     忽略异常为true
    exchange = @Exchange(value ="huangou.item.exchange",ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
    key = {"item.insert","item.update"}))
    public void save(Long supId) throws Exception{  //如果抛出异常spring会取消ack，如果无异常则会自动ack
        if (supId == null){
            return;
        }
        this.searchService.save(supId);
    }



    @RabbitListener(bindings = @QueueBinding ( value = @Queue(value = "huangou.search.delete.queue",durable = "true"),
            //交换机名称要和生产者交换机名字一样     忽略异常为true
            exchange = @Exchange(value ="huangou.item.exchange",ignoreDeclarationExceptions = "true",type = ExchangeTypes.TOPIC),
            key = {"item.delete"}))
    public void delete(Long supId) throws Exception{  //如果抛出异常spring会取消ack，如果无异常则会自动ack
        if (supId == null){
            return;
        }
        //删除elasticsearch里的这条数据
        this.searchService.delete(supId);
    }
}
