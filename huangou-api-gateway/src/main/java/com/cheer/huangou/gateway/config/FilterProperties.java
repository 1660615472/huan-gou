package com.cheer.huangou.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhangjie
 * @title:白名单配置类
 * @data
 */
@Data
@Component
@ConfigurationProperties(prefix = "hg.filter")
public class FilterProperties {

    private List<String> allowPaths;
}
