package com.amad.autotrip.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 클라이언트 라우팅 경로를 index.html로 포워드 (API와 정적 파일 제외)
        registry.addViewController("/**/{path:[^\\.]*}").setViewName("forward:/index.html");
    }
}
