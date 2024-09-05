package com.example.spring_security.request;


import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class CreateLessonRequest {

    @NotNull
    private String code;
    private String title;
    private String description;
    private Long courseId;
}
