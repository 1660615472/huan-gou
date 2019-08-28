package com.cheer.huangou.item;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.PropertySource;
import tk.mybatis.spring.annotation.MapperScan;


/**
 * @author zhangjie
 * @title:
 * @data
 */

@PropertySource("classpath:server.port.properties")
@MapperScan("com.cheer.huangou.item.mapper")
@EnableDiscoveryClient //能让注册中心扫描到该服务
@SpringBootApplication
public class HuangouItemService {
    public static void main(String[] args) {
        SpringApplication.run ( HuangouItemService.class,args );
    }
}
