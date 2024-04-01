package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration      // 스프링 빈으로 등록
public class WebMvcConfig implements WebMvcConfigurer {     // 스프링 MVC 애플리케이션의 구성(configuration)을 커스터마이징
    private final long MAX_AGE_SECS = 3600;
    @Override
    public void addCorsMappings(CorsRegistry registry) {    // CORS 관련 구성을 정의하는 메소드 (CORS 관련 정보 담은 클래스 CorsRegistry)
        registry.addMapping("/**")                  // 모든 경로(/**)에 대해
                .allowedOrigins("http://localhost:3000")      // Origin이 http://localhost:3000에 대해
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")     // GET, POST, PUT, PATCH, DELETE, OPTIONS 메소드를 이용한 요청은 허용한다
                .allowedHeaders("*")        // 모든 헤더와 인증에 관한 정보(credential)도 허용한다
                .allowCredentials(true)
                .maxAge(MAX_AGE_SECS);
    }
}
