package com.huangou.auth.controller;

import com.cheer.huangou.common.utils.CookieUtils;
import com.huangou.auth.config.JwtProperties;
import com.huangou.auth.service.AuthService;
import com.huangou.common.pojo.UserInfo;
import com.huangou.common.utils.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhangjie
 * @title: 授权中心控制器
 * @data
 */

@Controller
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录授权
     * @param username
     * @param password
     * @param request
     * @param response
     * @return
     */
    @PostMapping("accredit")
    public ResponseEntity<Void> accredit(@RequestParam("username") String username,
                                         @RequestParam("password") String password,
                                         HttpServletRequest request,
                                         HttpServletResponse response) throws Exception {
        String token = this.authService.accredit ( username, password );

        if(StringUtils.isBlank ( token )){  //HttpStatus.UNAUTHORIZED   未认证的意思
            return ResponseEntity.status ( HttpStatus.UNAUTHORIZED ).build ();
        }

        //设置cookie信息，返回给客户端浏览器                                              jwtProperties.getExpire () cookie存活时间
        CookieUtils.setCookie ( request, response, jwtProperties.getCookieName (), token,jwtProperties.getExpire ()*60 );

        return ResponseEntity.ok ( null ) ;
    }

    @GetMapping("verify")                //通过cookieValue注解获取域中cookie信息
    public ResponseEntity<UserInfo> verify(@CookieValue("HUAN_GOU") String token,HttpServletRequest request
    ,HttpServletResponse response){
        //通过jwt工具类获取解析JWT 使用公钥解析
        UserInfo userInfo = JwtUtils.getUserInfo ( this.jwtProperties.getPublicKey (), token );
        if(userInfo == null){
            return new ResponseEntity<> ( HttpStatus.UNAUTHORIZED );
        }

        //刷新jtw有效时间，只要用户还在使用就刷新jtw，有效时间从用户不适用开始
        //解决办法，重新生成一个jwt
        token = JwtUtils.generateToken ( userInfo,jwtProperties.getPrivateKey (),jwtProperties.getExpire () );

        //重新生成cookie
        CookieUtils.setCookie ( request,response,this.jwtProperties.getCookieName (),token,this.jwtProperties.getExpire ()*60 );


        //刷新cookie有效时间
        return ResponseEntity.ok ( userInfo );
    }

}
