package com.example.demo.persistence;

import com.example.demo.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    UserEntity findByEmail(String email);   // 이메일로 사용자 정보 찾기
    Boolean existsByEmail(String email);    // 이메일로 사용자 정보 존재 여부 확인
    UserEntity findByEmailAndPassword(String email, String password);   // 이메일, 비밀번호로 사용자 정보 찾기
}
