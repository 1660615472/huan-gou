package com.cheer.search.client;

import com.cheer.huangou.api.CategoryApi;
import com.cheer.huangou.model.Category;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author zhangjie
 * @title:    Feign实现远程调用 还可以做负载均衡！
 * @data
 */
@FeignClient("huangou-item-service") //远程调用这个服务
public interface CategoryClient extends CategoryApi {


}
