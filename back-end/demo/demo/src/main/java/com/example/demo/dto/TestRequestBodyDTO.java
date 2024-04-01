package com.example.demo.dto;

import lombok.Data;

@Data       // getter, setter 자동 생성
public class TestRequestBodyDTO {   // 전달된 값을 담는데 사용할 객체
    private int id;
    private String message;
}
