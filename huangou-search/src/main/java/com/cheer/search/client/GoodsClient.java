package com.cheer.search.client;

import com.cheer.huangou.api.GoodsApi;
import com.cheer.huangou.common.vo.PageResult;
import com.cheer.huangou.model.Sku;
import com.cheer.huangou.model.SpuDetail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author zhangjie
 * @title:
 * @data
 */
@FeignClient("huangou-item-service") //远程调用这个服务
public interface GoodsClient extends GoodsApi {//继承huangou-item-interface模块的GoodsApi接口

}
