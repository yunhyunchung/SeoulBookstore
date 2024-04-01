package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "email")})  // DDL 생성 시 유니크 제약 조건을 만든다 = 동일한 이메일 주소 중복 방지
public class UserEntity {   // 사용자 정보를 DB에 저장하는 엔터티 클래스
    // 이메일과 패스워드로 로그인한다
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;  // 사용자에게 고유하게 부여되는 ID
    @Column(nullable = false)   // NULL이 되면 안 됨
    private String username;    // 사용자 이름
    @Column(nullable = false)
    private String email;       // 사용자 EMAIL, 아이디와 같은 기능
    @Column(nullable = false)
    private String password;    // 패스워드
}