package com.example.demo.persistence;

import com.example.demo.model.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository     // 퍼시스턴스 레이어에 속함  (JpaRepository 인터페이스 확장)
public interface BookRepository extends JpaRepository<BookEntity, String> {     // <테이블에 매핑될 entity class, 이 entity의 기본 key의 타입>
    List<BookEntity> findByUserId(String userId);   // userId로 검색한 book entity list 결과 반환
    // 1) JPA Repository가 이 함수를 제공 X => findByUserId 메소드 이름 파싱 => 자동으로 db 쿼리 작성 후 실행
    // findByUserId 함수 => SELECT * FROM Book WHERE userId = '{userId}' 쿼리 자동 작성
    // 2) @Query annotation으로 직접 쿼리 추가 (?1 : 1번째 매개변수)
    // @Query(value="select * from Book b where b.userId = ?1", nativeQuery = true)
    List<BookEntity> findByTitle(String title);     // title로 검색한 book entity list 결과 반환
}
