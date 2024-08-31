package com.example.spring_security.request;


import lombok.Data;

import java.util.List;

@Data
public class CreateQuestionRequest {
    private Long courseId;
    private String content;
    private List<String> options;
    private String correctAnswer;
}
