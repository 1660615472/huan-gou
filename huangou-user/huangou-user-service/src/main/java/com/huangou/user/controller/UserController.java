package com.huangou.user.controller;

import com.huangou.user.pojo.User;
import com.huangou.user.service.UserService;
import com.netflix.ribbon.proxy.annotation.Http;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

/**
 * @author zhangjie
 * @title:
 * @data
 */

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/check/{data}/{type}")
    public ResponseEntity<Boolean> checkUser(@PathVariable(value = "data") String data,@PathVariable
            (value = "type") Integer type){
        //判断用户是否存在
       Boolean bool  = this.userService.checkUser(data,type);
       if(bool == null){
           return new ResponseEntity<> ( HttpStatus.BAD_REQUEST );
       }
       return ResponseEntity.ok ( bool );
    }

    //发送手机验证码
    @PostMapping("code")
    public ResponseEntity<Void> sendVerifyCode(@RequestParam("phone") String phone){
        this.userService.sendVerifyCode(phone);
        return ResponseEntity.status ( HttpStatus.CREATED ).build ();
    }



    @PostMapping("register")            //valid 数据校验
    public ResponseEntity<Void> register(@Valid User user, @RequestParam("code")String code)throws Exception{
            //效验验证码是否正确
       Boolean boo = this.userService.register(user,code);
       if(boo == null|| !boo){
           return new ResponseEntity<> ( HttpStatus.BAD_REQUEST );
       }

       return new ResponseEntity<> ( HttpStatus.CREATED );
    }

    //查询用户是否存在（登录）
    @GetMapping("query")
    public ResponseEntity<User> queryUser(@RequestParam("username")String username,@RequestParam
            ("password")String password){
            //查询数据库是否有这个用户
     User  user =  this.userService.queryUser(username,password);
     if(user == null){
         return ResponseEntity.status ( HttpStatus.BAD_REQUEST ).build ();
     }

     return ResponseEntity.ok ( user );
    }
}
