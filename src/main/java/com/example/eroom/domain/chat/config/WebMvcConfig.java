package com.example.eroom.domain.chat.config;

import com.example.eroom.domain.chat.interceptor.ProjectAccessInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final ProjectAccessInterceptor projectAccessInterceptor;

    public WebMvcConfig(ProjectAccessInterceptor projectAccessInterceptor) {
        this.projectAccessInterceptor = projectAccessInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(projectAccessInterceptor)
                .addPathPatterns("/project/**")
                .excludePathPatterns("/project/list", "/project/create");
    }
}
