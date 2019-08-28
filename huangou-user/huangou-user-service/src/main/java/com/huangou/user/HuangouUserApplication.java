package com.huangou.user;

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

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.huangou.user.mapper")
@PropertySource( "classpath:server.port.properties" )
public class HuangouUserApplication {
    public static void main(String[] args) {
        SpringApplication.run (HuangouUserApplication.class,args  );
    }
}
