package com.example.spring_security.request;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class CreateLessonRequest {

    private String title;
    private String description;
    private Long courseId;
}
