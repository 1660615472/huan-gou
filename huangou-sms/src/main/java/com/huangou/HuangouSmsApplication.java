package com.huangou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

/**
 * @author zhangjie
 * @title:
 * @data
 */

@SpringBootApplication
@PropertySource( "classpath:server.port.properties")
public class HuangouSmsApplication {
    public static void main(String[] args) {
        SpringApplication.run ( HuangouSmsApplication.class,args );
    }
}
