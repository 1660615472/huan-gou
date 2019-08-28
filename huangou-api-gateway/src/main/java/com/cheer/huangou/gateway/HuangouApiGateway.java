package com.cheer.huangou.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.PropertySource;

/**
 * @author zhangjie
 * @title:
 * @data
 */

@PropertySource("classpath:server.port.properties")
@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
public class HuangouApiGateway {
    public static void main(String[] args) {
        SpringApplication.run(HuangouApiGateway.class, args);
    }
}
