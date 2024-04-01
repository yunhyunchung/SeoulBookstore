package com.example.demo.service;

import com.example.demo.dto.BookDTO;
import com.example.demo.model.BookEntity;
import com.example.demo.persistence.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j      // 가장 많이 쓰이는 로깅 라이브러리
@Service
public class BookService {
    @Autowired      // TodoService(상위)가 TodoRepository(하위) 사용 (서비스 제공 받음)
    private BookRepository repository;      // persistence 객체 자동 주입

    public List<BookEntity> create(final BookEntity entity) {   // Book entity 생성
        // 1) Validation 검증 (저장할 entity가 유효한지 확인한다)
        validate(entity);

        // 2) 엔터티 저장 save()
        repository.save(entity);
        log.info("Entity id: {} is saved", entity.getId());  // book title 정보 확인

        // 3) 저장된 엔터티 찾아서 생성 후 새 BookList 반환
        return repository.findByUserId(entity.getUserId());
    }

    public List<BookEntity> retrieve(final String userId) {     // userId로 검색한 결과 list 리턴
        return repository.findByUserId(userId);         // (persistence) repository에 검색 요청
    }

    public List<BookEntity> retrieveTitle(final String title) {     // book title로 검색한 결과 list 리턴
        return repository.findByTitle(title);         // (persistence) repository에 검색 요청
    }

    public BookEntity update(final BookEntity entity) {   // 수정할 엔터티 (원래 함수)
        //(1) 저장할 엔터티가 유효한지 확인.
        validate(entity);

        //(2) 넘겨받은 엔터티 id를 이용해 DB에 있는 BookEntity를 가져온다. 존재하지 않는 엔터티는 업데이트 X
        final Optional<BookEntity> original = repository.findById(entity.getId());      // Optional 클래스 : null일 수도 있는 객체도 저장 (null pointer exception 방지)

        //(3) 반환된 BookEntity가 존재하면, 값을 수정할 새 entity 값으로 덮어 씌운다.
        original.ifPresent (book -> {
            book.setTitle(entity.getTitle());
            book.setAuthor(entity.getAuthor());
            book.setPublisher(entity.getPublisher());
            book.setUserId(entity.getUserId());
            repository.save(book);
        });

        // (4) 수정한 책 정보만 컨트롤러에 리턴
        return entity;
    }

    public List<BookEntity> delete(final BookEntity entity) {   // 삭제할 entity
        validate(entity);   //(1) 저장할 엔터티가 유효한지 확인

        try {
            repository.delete(entity);    // (2) 엔터티를 삭제한다.
        } catch(Exception e) {           // 문제 발생 시 id, exception 로깅 & 컨트롤러로 exception 객체 리턴
            log.error("error deleting entity ", entity.getId(), e);
            throw new RuntimeException("error deleting entity " + entity.getId());
        }
        return retrieve(entity.getUserId());    // (5) 삭제 후 새 book 리스트 리턴
    }

    public List<BookEntity> deleteByTitle(String title) {
        List<BookEntity> entities = repository.findByTitle(title);
        repository.deleteAll(entities);
        List<BookEntity> remainingEntities = repository.findAll();  // 삭제 후 남은 모든 엔티티들을 조회하여 반환
        return remainingEntities;
    }


    // Refactoring한 Validation 검증 메소드(실제 제대로 된 entity인지 검사)
    private void validate(final BookEntity entity) {
        if(entity == null) {
            log.warn("Entity cannot be null.");
            throw new RuntimeException("Entity cannot be null.");
        }

        if(entity.getUserId() == null) {
            log.warn("Unknown user.");
            throw new RuntimeException("Unknown user.");
        }
    }

}
