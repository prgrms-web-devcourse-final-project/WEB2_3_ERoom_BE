package com.example.eroom.domain.chat.config;

import com.example.eroom.domain.chat.interceptor.ProjectAccessInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final ProjectAccessInterceptor projectAccessInterceptor;



    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // Interceptor (REST API)
        registry.addInterceptor(projectAccessInterceptor)
                .addPathPatterns("/api/projects/**")
                .excludePathPatterns("/api/projects/list", "/api/projects/create");
    }
}
