package com.example.demo.security;

import com.example.demo.model.UserEntity;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
public class TokenProvider {    // 사용자 정보를 받아 JWT 생성하는 클래스
    private static final String SECRET_KEY = "NMA8JPctFuna59f5";    // 응답에서 토큰 복사

    public String create(UserEntity userEntity) {   // 토큰 생성 메소드
        // 토큰 만료 기한은 지금부터 1일
        Date expiryDate = Date.from(
                Instant.now().plus(1, ChronoUnit.DAYS)
        );

        // JWT Token 생성 (jjwt 라이브러리 이용)
        return Jwts.builder()
                // header에 들어갈 내용 및 서명을 하기 위한 SECRET_KEY
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                // payload에 들어갈 내용 (사용자 정보 등을 이용해서 토큰 내부의 페이로드 작성)
                //.setSubject(userEntity.getId())       // 사용자 아이디 (book id) sub
                .setSubject(userEntity.getUsername())   // 사용자 아이디 (userId) sub
                .setIssuer("demo app")              // iss 토큰 발행자(서버)
                .setIssuedAt(new Date())            // iat 토큰 발행일
                .setExpiration(expiryDate)          // exp 토큰 만료일
                .compact();

        /* 토큰 example
        {  // header
            "alg": "HS512"
        }
        {  // payload
            "sub": "40200000023040230390" // 사용자 아이디 subject
            "iss": "demo app",      // 발행자 issuer
            "iat": 1595733657,      // 발행일 issued at
            "exp": 1596597657       // 토큰 기한 만료일
        }
        // SECRET_KEY를 이용해 서명한 부분
        //Nn4d---------------
         */
    }

    public String validateAndGetUserId(String token) {  // 토큰의 유효성 검사 & 사용자 id 반환
        // 시크릿 서명과 token 서명 비교 후 위조되지 않았다면 페이로드(Claims) 리턴, 위조면 예외 날림
        Claims claims = Jwts.parser()       // Claims: payload에 담긴 key/value 정보(claim이라고 함)를 담는 객체
                .setSigningKey(SECRET_KEY)  // header와 payload를 setSigningKey로 넘어온 시크릿 키를 이용해 서명한 후
                .parseClaimsJws(token)      // Base64로 디코딩 및 파싱. token의 서명과 비교
                .getBody();                 // Payload의 body 부분을 얻어서

        return claims.getSubject();     // userEntity의 id를 얻음
    }

}