package com.cheer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;

/**
 * @author zhangjie
 * @title:搜索服务启动类
 * @data
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@PropertySource( "classpath:server.port.properties" )
public class huangouSearchApplication {
    public static void main(String[] args) {
        SpringApplication.run (huangouSearchApplication.class,args);
    }
}
