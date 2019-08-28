package com.huangou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author zhangjie
 * @title:
 * @data
 */

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class HuangouAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run ( HuangouAuthApplication.class,args );
    }
}
