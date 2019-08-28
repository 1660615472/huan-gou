package com.cheer.huangou.goods.client;

import com.cheer.huangou.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author zhangjie
 * @title:    Feign实现远程调用 还可以做负载均衡！
 * @data
 */
@FeignClient("huangou-item-service") //远程调用这个服务
public interface CategoryClient extends CategoryApi {


}
