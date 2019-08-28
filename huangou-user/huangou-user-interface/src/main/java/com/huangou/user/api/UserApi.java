package com.huangou.user.api;

import com.huangou.user.pojo.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zhangjie
 * @title:用户服务功能远程调用 api接口
 * @data
 */
public interface UserApi {

    //查询用户是否存在（登录）
    @GetMapping("query")
    User queryUser(@RequestParam("username")String username, @RequestParam
            ("password")String password);
}
