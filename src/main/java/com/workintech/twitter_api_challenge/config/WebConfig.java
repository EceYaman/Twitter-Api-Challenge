package com.workintech.twitter_api_challenge.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3200")
                .allowedMethods("*")
                .allowedHeaders("Authorization","Content-Type","Accept")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
