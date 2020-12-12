package com.yb.project.config;

import com.yb.common.utils.OSSTemplate;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OSSConfig {

    @Bean
    @ConfigurationProperties(prefix = "oss")
    public OSSTemplate getOSSTemplate() {
        return new OSSTemplate();
    }
}
