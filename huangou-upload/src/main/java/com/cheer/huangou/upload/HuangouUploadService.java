package com.cheer.huangou.upload;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.PropertySource;

/**
 * @author zhangjie
 * @title:
 * @data
 */

@PropertySource ( "classpath:server.port.properties" )
@SpringBootApplication
@EnableDiscoveryClient
public class HuangouUploadService {
    public static void main(String[] args) {
        SpringApplication.run ( HuangouUploadService.class,args );
    }
}
