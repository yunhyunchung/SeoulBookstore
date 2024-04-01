package com.example.demo.config;

import com.example.demo.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.CorsFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity  // 스프링 시큐리티 설정을 담당하는 클래스임을 선언하는 어노테이션 (스프링 시큐리티 활성화, 기본적으로 csrf 공격 보호)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;    // 구현했던 Servlet Filter
    @Bean
    public PasswordEncoder passwordEncoder() {      // PasswordEncoder 빈(Bean)을 직접 정의
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {      // HttpSecurity(시큐리티 설정을 위한 객체)를 통해 보안 관련 설정을 함
        try {
            // http 시큐리티 빌더 (세부적인 보안 기능을 설정하는 API 제공하는 클래스)
            http.cors()                     // CorsFilter 추가 (WebMvcConfig에서 이미 설정, CORS pre-flight request 처리하는 필터)
                    .and()
                    .csrf().disable()       // csrf 공격 보호 비활성화
                    .httpBasic().disable()  // token 사용하므로 basic 인증 비활성화
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // session 기반이 아님을 선언
                    .and()
                    .authorizeRequests().antMatchers("/", "/auth/**").permitAll()   // /와 /auth/** 경로는 인증 안 해도 됨
                        // 이 부분이 없다면, localhost:8080 또는 localhost:8080/auth/signup 요청 시, status code 403 (forbiddein)이 리턴됨
                    .anyRequest().authenticated();  // 이외의 모든 경로는 인증 해야 됨.

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // filter 등록
        // 매 요청마다 CorsFilter 실행하고 나서, jwtAuthenticationFilter를 실행해라
        http.addFilterAfter(
                jwtAuthenticationFilter,    // 뒤에 실행
                CorsFilter.class            // 먼저 실행
        );
    }
}
