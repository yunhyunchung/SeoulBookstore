package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder
@NoArgsConstructor      // 1) 매개변수 없는 생성자 생성
@AllArgsConstructor
@Data                   // 2) Getter, Setter 추가

@Entity                 // Entity 클래스 지정
@Table(name = "Book")   // 이 엔터티 클래스와 연결된 테이블 이름 지정
public class BookEntity {
    @Id     // 기본 키 지정
    @GeneratedValue(generator="system-uuid")                // generator 이용하겠다
    @GenericGenerator(name="system-uuid", strategy="uuid")  // id를 uuid 문자열로 자동으로 생성하는 generator 정의
    private String id;          // 책 id
    private String title;       // 책 제목
    private String author;      // 책 저자
    private String publisher;   // 책 출판사
    private String userId;      // 사용자 아이디
}
