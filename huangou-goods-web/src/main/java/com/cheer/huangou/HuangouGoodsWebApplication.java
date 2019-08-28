package com.cheer.huangou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;

/**
 * @author zhangjie
 * @title:
 * @data
 */
@PropertySource("classpath:server.port.properties")
@EnableFeignClients
@SpringBootApplication
@EnableDiscoveryClient
public class HuangouGoodsWebApplication {
    public static void main(String[] args) {
        SpringApplication.run (HuangouGoodsWebApplication.class,args);
    }
}
