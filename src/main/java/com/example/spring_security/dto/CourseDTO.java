package com.example.spring_security.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class CourseDTO {

    private Long id;
    private String code;
    private String title;
    private String description;
    private String language;
}

