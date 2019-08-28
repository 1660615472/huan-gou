package com.huangou.auth.client;

import com.huangou.user.api.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author zhangjie
 * @title:
 * @data
 */

@FeignClient(value = "user-service") //注意这里调用的是服务名字不是项目文件名字
public interface UserClient extends UserApi {
}
