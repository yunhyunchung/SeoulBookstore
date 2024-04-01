package com.example.demo.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseDTO<T> {      // HTTP 응답으로 사용할 DTO
    private String error;   // 클라이언트로 반환 시 부가적인 에러 메시지 추가
    private List<T> data;   // 자바 Generic Type (모든 원소 타입 리스트를 반환 가능)
}
