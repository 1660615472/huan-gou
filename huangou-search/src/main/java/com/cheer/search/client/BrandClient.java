package com.cheer.search.client;

import com.cheer.huangou.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author zhangjie
 * @title:
 * @data
 */
@FeignClient("huangou-item-service") //远程调用这个服务
public interface BrandClient extends BrandApi {
}
