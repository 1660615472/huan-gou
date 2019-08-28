package com.huangou.auth.service;

import com.huangou.auth.client.UserClient;
import com.huangou.auth.config.JwtProperties;
import com.huangou.common.pojo.UserInfo;
import com.huangou.common.utils.JwtUtils;
import com.huangou.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

/**
 * @author zhangjie
 * @title:
 * @data
 */
@EnableConfigurationProperties(JwtProperties.class)
@Service
public class AuthService {
    @Autowired
    private UserClient userClient; //远程调用来的usersercie服务接口中的功能
    @Autowired
    private JwtProperties jwtProperties;



    //用户登录授权
    public String accredit(String username, String password) throws Exception {



        //生成载荷
        try {
            //根据用户名和密码查询
            User user = userClient.queryUser ( username, password );
            //判断user是否为空
            if(user == null){
                return null;
            }
            UserInfo userInfo = new UserInfo ();
            //赋值
            userInfo.setId ( user.getId () );
            userInfo.setName ( user.getUsername () );
            //不为空就用jwtutils生成jwt类型的token
                                                  //jwt的载荷          私钥                      过期时间
           return JwtUtils.generateToken ( userInfo, this.jwtProperties.getPrivateKey (), jwtProperties.getExpire () );
        }catch (Exception e){
            e.printStackTrace ();
            throw new Exception ( "【授权中心】用户名和密码错误" );
        }

    }
}
