package com.huangou.common.utils;




import com.huangou.common.pojo.UserInfo;
import javafx.application.Application;

import org.junit.Before;
import org.junit.Test;

import org.springframework.test.context.junit4.SpringRunner;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author bystander
 * @date 2018/10/1
 */


public class JwtUtilsTest {

    private static final String publicKeyPath = "C:\\tmp\\rsa\\rsa.pub";
    private static final String privateKeyPath = "C:\\tmp\\rsa\\rsa.pri";

    private PrivateKey privateKey;
    private PublicKey publicKey;


    @Test //生成公钥和私钥
    public void testRsa() throws Exception {
        RsaUtils.generateKey(publicKeyPath, privateKeyPath, "234");
    }

    @Before
    public void testGetRsa() throws Exception {
        privateKey = RsaUtils.getPrivateKey(privateKeyPath);
        publicKey = RsaUtils.getPublicKey(publicKeyPath);
    }

    @org.junit.Test
    public void generateToken() {
        //生成Token                         //载荷内容
        String s = JwtUtils.generateToken(new UserInfo (20L, "Jack"), privateKey, 5);
        System.out.println("s = " + s);
    }


    @org.junit.Test
    public void parseToken() {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjAsInVzZXJuYW1lIjoiSmFjayIsImV4cCI6MTU2NjU0NDg1OX0.Z7guGE4EaswCZBQm3DMRDHtTzbBAWme9Yf-yZwp1-PhT81M01gmwDvrKlPRiYBmbCVByrkGrz-0VTi_NhYE8YfLeSwPbhJuS3gJOos8jmnr27YTCXR2-XtMMQMfBfBJBQj_2sj1_NMo4dQu5m1Fnt2aI3oyHNeTSYoLTTR9gVyo";
        //解析token
        UserInfo userInfo = JwtUtils.getUserInfo(publicKey, token);
        System.out.println("id:" + userInfo.getId());
        System.out.println("name:" + userInfo.getName());
    }

    @org.junit.Test
    public void parseToken1() {
    }

    @org.junit.Test
    public void getUserInfo() {
    }

    @org.junit.Test
    public void getUserInfo1() {
    }
}