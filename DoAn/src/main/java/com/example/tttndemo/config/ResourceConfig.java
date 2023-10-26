package com.example.tttndemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class ResourceConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/product/**")
                .addResourceLocations("/public", "classpath:/static/");
        registry.addResourceHandler("/address/**")
                .addResourceLocations("/public", "classpath:/static/");
        registry.addResourceHandler("/address/update/**")
                .addResourceLocations("/public", "classpath:/static/");
        registry.addResourceHandler("/order/**")
                .addResourceLocations("/public", "classpath:/static/");
        registry.addResourceHandler("/forgot-password/**")
                .addResourceLocations("/public", "classpath:/static/");


    }
}
