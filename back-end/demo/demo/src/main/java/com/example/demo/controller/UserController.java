package com.example.demo.controller;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.model.UserEntity;
import com.example.demo.security.TokenProvider;
import com.example.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("auth")
public class UserController {     // 사용자 등록(회원가입), 인증(로그인) 시 호출 메소드 구현
    @Autowired
    private UserService userService;
    @Autowired
    private TokenProvider tokenProvider;    // 토큰을 생성해서 제공하는 클래스

    //@Autowired
    //private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();   // 스프링 시큐리티가 제공하는 패스워드 암호화 클래스  // Bean으로 작성해도 됨
    private final PasswordEncoder passwordEncoder;      // 직접 정의한 PasswordEncoder 빈bean 사용
    @Autowired
    public UserController(@Qualifier("passwordEncoder") PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signup")     // 회원가입 시 호출되는 메소드
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {   // 회원가입 시 사용자 등록함
        try {
            // 1) UserDTO 담은 요청을 이용해 저장할 사용자 UserEntity 만들기
            UserEntity user = UserEntity.builder()
                    .email(userDTO.getEmail())
                    .username(userDTO.getUsername())
                    .password(passwordEncoder.encode(userDTO.getPassword()))    // 패스워드가 암호화되어 저장됨
                    .build();

            // 2) service를 이용해 사용자 UserEntity 생성 후, 리포지토리 db에 UserDTO로 저장
            UserEntity registeredUser = userService.create(user);
            UserDTO responseUserDTO = UserDTO.builder()
                    .email(registeredUser.getEmail())
                    .id(registeredUser.getId())
                    .username(registeredUser.getUsername())
                    // .password(registeredUser.getPassword())
                    .build();

            return ResponseEntity.ok().body(responseUserDTO);
        } catch (Exception e) {
            // 사용자 정보는 항상 하나이므로 리스트 ResponseDTO 사용하지 않고 그냥 UserDTO 하나 리턴
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }

    @PostMapping("/signin")     // 로그인 시 호출되는 메소드
    public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO) {   // 로그인 시 사용자 인증함
        // 1) service로 리포지터리에서 사용자 정보 검색, 확인 (UserEntity 검색)
        UserEntity user = userService.getByCredentials(     // 로그인 전에 사용자 찾기
                userDTO.getEmail(),
                userDTO.getPassword(),
                passwordEncoder
        );
        // 만약 DB에 해당 사용자가 있으면 로그인 O
        if (user != null) {
            // (2) 토큰 생성
            final String token = tokenProvider.create(user);

            // (3) UserDTO인 responseUserDTO 생성, responseEntity 반환
            final UserDTO responseUserDTO = UserDTO.builder()
                    .email(user.getEmail())
                    .id(user.getId())
                    // .password(user.getPassword())
                    .token(token)       // 로그인 후 토큰을 클라이언트에게 반환
                    .build();
            return ResponseEntity.ok().body(responseUserDTO);

        } else {            // 해당 사용자가 없으면 로그인 X
            ResponseDTO responseDTO = ResponseDTO.builder().error("Login failed").build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}
