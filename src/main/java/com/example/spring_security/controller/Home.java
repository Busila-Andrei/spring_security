package com.example.spring_security.controller;


import com.example.spring_security.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/")
public class Home {

    @GetMapping("/home")
    public ResponseEntity<ApiResponse> home() {
        return ResponseEntity.ok(new ApiResponse("Success", "Hello world!"));
    }
}
