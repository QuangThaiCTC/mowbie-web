package com.mowbie.mowbie_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Cho phép tất cả endpoint
                        .allowedOrigins("http://192.168.10.1:5173") // Cho phép frontend
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Hỗ trợ tất cả phương thức
                        .allowedHeaders("*") // Cho phép tất cả headers
                        .allowCredentials(true); // Cho phép gửi Cookie (Refresh Token)
            }
        };
    }
}
