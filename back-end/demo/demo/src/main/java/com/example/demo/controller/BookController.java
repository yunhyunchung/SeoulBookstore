package com.example.demo.controller;

import com.example.demo.dto.BookDTO;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.model.BookEntity;
import com.example.demo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.list;

@RestController
@RequestMapping("book")
public class BookController {
    @Autowired
    private BookService service;    // Service 객체 자동 주입

    @PostMapping        // Book Item 생성 & 새로운 Book list 리턴
    public ResponseEntity<?> createBook(@AuthenticationPrincipal String userId, @RequestBody BookDTO dto) {
        try {
            //(1) BookDTO를 BookEntity로 변환한다.
            BookEntity entity = BookDTO.toEntity(dto);
            entity.setId(null);
            entity.setUserId(userId);   // @AuthenticationPrincipal에서 넘어온 userId로 설정
                                        // 예전: entity.setUserId(entity.getUserId());
            //(2) 서비스 계층에 요청해서 BookEntity 생성 & 생성 후 새로운 entity list 반환
            List<BookEntity> entities = service.create(entity);
            List<BookDTO> dtos = entities.stream().map(BookDTO::new).collect(Collectors.toList());  //(3) 리턴된 Entity list를 DTO 리스트로 변환한다.
            ResponseDTO<BookDTO> response = ResponseDTO.<BookDTO>builder().data(dtos).build();  //(4) 변환된 DTO 리스트로 http 응답 DTO 생성하고 ResponseEntity에 실어서 반환

            return ResponseEntity.ok().body(response);  // 현재 존재하는 모든 제품 리스트를 status code와 함께 클라이언트에 반환함

        } catch (Exception e) {
            //(8) 혹시 예외가 있는 경우 dto 대신 error에 메시지 넣어 리턴
            String error = e.getMessage();
            ResponseDTO<BookDTO> response = ResponseDTO.<BookDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> retrieveAllBookList(@AuthenticationPrincipal String userId) {       // localhost:8080/book/all  // 검색 결과 모든 book list 반환
        try {
            //(1) Service에게 요청해 retrieve() 메소드로 userId 검색해서 검색결과 모든 book list 가져온다.
            List<BookEntity> entities = service.retrieve(userId);
            //System.out.println("Entities size: " + entities.size()); // entities 리스트의 크기를 출력

            List<BookDTO> dtos = entities.stream().map(BookDTO::new).collect(Collectors.toList());      //(2) 자바 스트림을 이용해 리턴된 entity 리스트 => BookDTO 리스트로 변환한다.
            ResponseDTO<BookDTO> response = ResponseDTO.<BookDTO>builder().data(dtos).build();          //(3) 변환된 BookDTO 리스트를 이용해 ResponseDTO를 초기화
            return ResponseEntity.ok().body(response);                                                  //(4) ResponseDTO를 실은 ResponseEntity 반환
        } catch (Exception e) {
            // 예외가 발생하면 예외 메시지를 클라이언트에 반환
            String error = e.getMessage();
            ResponseDTO<BookDTO> response = ResponseDTO.<BookDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> retrieveBookList(@RequestParam(required = false) String title) {   // localhost:8080/book/?title={title}
        try {
            //(1) Service의 retrieveTitle() : book title 검색 결과 Book list 가져온다.
            List<BookEntity> entities = service.retrieveTitle(title);

            //(2) 검색된 모든 제품 리스트를 status code와 함께 클라이언트에 반환
            // BookEntity list를 BookDTO list로 변환 => ResponseDTO 생성 후 ResponseEntity 응답 반환
            List<BookDTO> dtos = entities.stream().map(BookDTO::new).collect(Collectors.toList());
            ResponseDTO<BookDTO> response = ResponseDTO.<BookDTO>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            // 예외가 발생하면 예외 메시지를 클라이언트에 반환
            String error = e.getMessage();
            ResponseDTO<BookDTO> response = ResponseDTO.<BookDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateBook(@AuthenticationPrincipal String userId, @RequestBody BookDTO dto) {     // @PathVariable("id") String id,  // 수정할 book item
        //String temporaryUserId = "temporary-user";
        BookEntity entity = BookDTO.toEntity(dto);  //(1) dto를 entity로 변환
        entity.setUserId(userId);       // 예전: entity.setUserId(entity.getUserId());

        // (3) 서비스를 이용해 entity 수정하고, 수정한 책만 얻기
        BookEntity updatedBook = service.update(entity);
        // (4) 수정된 제품 정보를 리스트에 담아서 ResponseDTO로 반환
        BookDTO updatedDTO = new BookDTO(updatedBook);
        List<BookDTO> dataList = new ArrayList<>();
        dataList.add(updatedDTO);
        ResponseDTO<BookDTO> response = ResponseDTO.<BookDTO>builder().data(dataList).build();
        // (5) 수정된 제품의 정보 + status code => 클라이언트에 반환
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping      // 제품 정보 테이블의 삭제 버튼과 연결
    public ResponseEntity<?> deleteBook(@AuthenticationPrincipal String userId, @RequestBody BookDTO dto) {     // 삭제할 dto  // @PathVariable("title") String title  // @RequestParam String title
        try {
            // String temporaryUserId = "temporary-user";
            BookEntity entity = BookDTO.toEntity(dto);   //(1) DTO를 Entity로 변환
            entity.setUserId(userId);       // 예전: entity.setUserId(entity.getUserId());

            // (3) 서비스를 이용해 entity 삭제
            // BookEntity deleteEntity = (BookEntity)service.retrieveTitle(title);
            List<BookEntity> entities = service.delete(entity);

            // (3) 서비스를 이용해 title에 해당하는 제품을 삭제
            //List<BookEntity> entities = service.deleteByTitle(title);

            //(4) 삭제 후의 모든 제픔 리스트를 클라이언트에게 반환
            List<BookDTO> dtos = entities.stream().map(BookDTO::new).collect(Collectors.toList());
            ResponseDTO<BookDTO> response = ResponseDTO.<BookDTO>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            // 삭제 시 오류가 나면 dto 대신 error에 메시지 넣어 리턴
            String error = e.getMessage();
            ResponseDTO<BookDTO> response = ResponseDTO.<BookDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{title}")      // /book/{title} 경로로 DELETE 요청을 처리
    public ResponseEntity<?> deleteProduct(@PathVariable("title") String title) {
        try {
            List<BookEntity> entities = service.deleteByTitle(title);
            List<BookDTO> dtos = entities.stream().map(BookDTO::new).collect(Collectors.toList());
            ResponseDTO<BookDTO> response = ResponseDTO.<BookDTO>builder().data(dtos).build();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<BookDTO> response = ResponseDTO.<BookDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }

}
