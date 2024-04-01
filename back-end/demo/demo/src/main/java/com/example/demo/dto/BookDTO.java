package com.example.demo.dto;

import com.example.demo.model.BookEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookDTO {
    private String id;          // 책 id
    private String title;       // 책 제목
    private String author;      // 책 저자
    private String publisher;   // 책 출판사
    private String userId;      // 사용자 아이디

    // BookEntity 속성 값 받아서 DTO 생성 => 클라이언트에게 반환
    public BookDTO(final BookEntity entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.author = entity.getAuthor();
        this.publisher = entity.getPublisher();
        this.userId = entity.getUserId();   // Term Prj1: userId는 숨김 (DB 구조 캡슐화)
    }

    // 클라이언트에게 받은 DTO를 Entity로 변환 => Controller는 변환된 Entity를 Service에게 전달
    public static BookEntity toEntity(BookDTO dto) {
        return BookEntity.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .publisher(dto.getPublisher())
                .userId(dto.getUserId())
                .build();
    }
}
