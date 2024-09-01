package com.example.spring_security.request;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class CreateExerciseRequest {

    private String question;
    private String answer;
    private Long LessonId;
}
