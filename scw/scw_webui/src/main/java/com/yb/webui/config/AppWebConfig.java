package com.yb.webui.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppWebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        /*
            addViewController：访问的页面路径
            setViewName：显示的视图（模板中的页面）
         */
        registry.addViewController("/login.html").setViewName("login");
    }
}
