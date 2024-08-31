package com.example.spring_security.controller;


import com.example.spring_security.request.CreateQuestionRequest;
import com.example.spring_security.response.ApiResponse;
import com.example.spring_security.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/questions")
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createQuestions(@RequestBody CreateQuestionRequest request) {
        return ResponseEntity.ok(questionService.createQuestion(request));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<ApiResponse<?>> getQuestionsByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(questionService.getQuestionByCourse(courseId));
    }
}
