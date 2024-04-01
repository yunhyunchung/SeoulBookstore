package com.example.demo.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // 1) 사용자 요청에서 토큰 가져오기
            String token = parseBearerToken(request);
            log.info("Filter is running...");
            // 2) 토큰 검사하기
            if (token != null && !token.equalsIgnoreCase("null")) {
                String userId = tokenProvider.validateAndGetUserId(token);  // userId 가져오기. 위조된 경우 예외 처리된다.
                log.info("Authenticated user ID : " + userId);
                // 인증 완료 => UsernamePasswordAuthenticationToken 객체를 생성함
                AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(  //  사용자 아이디(principal), 비밀번호(credential) 등의 인증 정보를 담는 token 객체
                        userId,     // 인증된 사용자의 정보 (principal: 사용자 id, 접근 주체)
                        null,       // credentials (비밀번호)
                        AuthorityUtils.NO_AUTHORITIES   // 사용자 권한 없음 (<GrantedAuthority> authorities)
                );
                // authentication 객체를 SecurityContext 객체에 담아서 SecurityContextHolder에 등록
                // SecurityContextHolder에 등록해야 인증된 사용자라고 생각한다.
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                securityContext.setAuthentication(authentication);
                SecurityContextHolder.setContext(securityContext);
            }

        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }
        // 사용자 인증 후 다음 ServletFilter를 실행함
        filterChain.doFilter(request, response);
    }

    private String parseBearerToken(HttpServletRequest request) {
        // Http 요청의 header를 파싱해 Bearer 토큰을 리턴한다.
        String bearerToken = request.getHeader("Authorization");    // 인증 관련 헤더 값 얻음
        // bearerToken 문자열이 유효한 문자열인지 검사 => <TOKEN> 문자열 리턴
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);    // "Bearer " 다음에 위치한 문자열인 토큰을 가져온다. (인덱스 7부터 끝까지 문자열을 자름)
        }
        return null;
    }
}
