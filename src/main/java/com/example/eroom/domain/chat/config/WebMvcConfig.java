package com.example.eroom.domain.chat.config;

import com.example.eroom.domain.chat.interceptor.ProjectAccessInterceptor;
import com.example.eroom.domain.chat.thymeleaf.interceptor.ThymeleafProjectAccessInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final ThymeleafProjectAccessInterceptor thymeleafProjectAccessInterceptor;
    private final ProjectAccessInterceptor projectAccessInterceptor;

    public WebMvcConfig(ThymeleafProjectAccessInterceptor thymeleafProjectAccessInterceptor,
                        ProjectAccessInterceptor projectAccessInterceptor) {
        this.thymeleafProjectAccessInterceptor = thymeleafProjectAccessInterceptor;
        this.projectAccessInterceptor = projectAccessInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(thymeleafProjectAccessInterceptor)
                .addPathPatterns("/project/**")
                .excludePathPatterns("/project/list", "/project/create");

        // Interceptor (REST API)
        registry.addInterceptor(projectAccessInterceptor)
                .addPathPatterns("/api/projects/**")
                .excludePathPatterns("/api/projects/list", "/api/projects/create");
    }
}
