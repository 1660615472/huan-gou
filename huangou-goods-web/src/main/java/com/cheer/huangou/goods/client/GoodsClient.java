package com.cheer.huangou.goods.client;

import com.cheer.huangou.api.GoodsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author zhangjie
 * @title:
 * @data
 */
@FeignClient("huangou-item-service") //远程调用这个服务
public interface GoodsClient extends GoodsApi {//继承huangou-item-interface模块的GoodsApi接口

}
