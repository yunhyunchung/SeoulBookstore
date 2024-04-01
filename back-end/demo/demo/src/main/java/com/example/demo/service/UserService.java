package com.example.demo.service;

import com.example.demo.model.UserEntity;
import com.example.demo.persistence.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;  // UserRepository 이용해서 사용자 생성

    public UserEntity create(final UserEntity userEntity) {     // UserRepository를 이용해서 생성한 사용자 UserEntity를 DB에 저장하는 메소드
        // 1) 사용자와 사용자 이메일이 비어있는지 null 검증
        if(userEntity == null || userEntity.getEmail() == null) {
            throw new RuntimeException("Invalid arguments");
        }
        // 2) DB에 있는 사용자 이메일 가져와서, 동일한 이메일 주소가 이미 DB에 존재하는지 확인
        final String email = userEntity.getEmail();
        if (userRepository.existsByEmail(email)) {
            log.warn("Email already exists {}", email);
        }
        return userRepository.save(userEntity);     // 3) 새로 생성한 UserEntity를 DB에 저장
    }

    public UserEntity getByCredentials(final String email, final String password, final PasswordEncoder encoder) {
        // 이메일과 패스워드를 이용해서 사용자 UserEntity를 검색 후 리턴 => 로그인 인증 정보 확인
        //return userRepository.findByEmailAndPassword(email, password);

        // 사용자가 입력한 패스워드와 DB에 저장된 패스워드와 일치하면 해당 사용자 엔터티를 반환하는 함수
        final UserEntity originalUser = userRepository.findByEmail(email);

        // 패스워드 인코더 matches()를 이용해서 2개의 패스워드가 동일한지 검사함 (사용자가 입력한 패스워드 == DB에 저장된 패스워드 (모두 암호화됨))
        if (originalUser != null && encoder.matches(password, originalUser.getPassword())) {
            return originalUser;        // 동일하면 회원 인증됨 => 사용자 UserEntity 반환
        }
        return null;
    }
}

