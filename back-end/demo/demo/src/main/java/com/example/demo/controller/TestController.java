package com.example.demo.controller;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.TestRequestBodyDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("test")
public class TestController {
    @GetMapping("/testGetMapping")
    public String testController() {
        return "Hello testGetMapping";
    }
    @GetMapping("/{id}")
    public String testControllerWithPathVariables(@PathVariable(required = false) int id) {
        return "Hello PathVariables " + id;     //  /test/123
    }
    @GetMapping("/testRequestParam")
    public String testControllerRequestParam(@RequestParam(required = false) int id) {
        return "Hello RequestParam " + id;      //  /test/testRequestParam?id=123
    }
    @GetMapping("/testRequestBody")
    public String testControllerRequestBody(@RequestBody TestRequestBodyDTO testRequestBodyDTO) {
        return "Hello RequestBody " + testRequestBodyDTO.getId()
                + ", message: " + testRequestBodyDTO.getMessage();
    }

    @GetMapping("/testResponseBody")
    public ResponseDTO<String> testControllerResponseBody() {   // DTO만 반환
        List<String> list = new ArrayList<>();
        list.add("1 first");
        list.add("2 second");
        ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();

        return response;
    }

    @GetMapping("/testResponseEntity")
    public ResponseEntity<?> testControllerResponseEntity() {   // DTO 객체 & 서버 status 등 추가 정보와 같이 응답
        List<String> list = new ArrayList<>();
        list.add("Hello ResponseEntity & status 404(bad request)");
        ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();

        return ResponseEntity.ok().body(response);  // http response: 서버 status 200 & DTO 객체를 JSON으로 응답 반환
        //return ResponseEntity.badRequest().body(response);   // 서버 status 404
    }
}
