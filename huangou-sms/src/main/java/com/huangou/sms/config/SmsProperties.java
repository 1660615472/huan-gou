package com.huangou.sms.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

/**
 * @author zhangjie
 * @title: 配置文件属性读取类
 * @data
 */
@Data
@ConfigurationProperties(prefix = "hg.sms")
public class SmsProperties {

    String accessKeyId;

    String accessKeySecret;

    String signName;

    String verifyCodeTemplate;
}
