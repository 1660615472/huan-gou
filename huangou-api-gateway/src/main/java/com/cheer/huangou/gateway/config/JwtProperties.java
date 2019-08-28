package com.cheer.huangou.gateway.config;

import com.huangou.common.utils.RsaUtils;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author zhangjie
 * @title:读取配置文件信息类，
 * @data
 */
@Log4j2
@Data
@Component
@ConfigurationProperties(prefix = "hg.jwt")
public class JwtProperties {

    private String pubKeyPath;

    private String cookieName;

    private PublicKey publicKey;


    @PostConstruct
    public void init() {
        try {
            //获取公钥
            this.publicKey = RsaUtils.getPublicKey ( pubKeyPath );
        } catch (Exception e) {
            log.error ( "初始化公钥私钥失败", e );
            throw new RuntimeException ();
        }
    }
}
