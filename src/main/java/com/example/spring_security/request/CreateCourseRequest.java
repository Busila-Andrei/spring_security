package com.example.spring_security.request;

import lombok.Data;

@Data
public class CreateCourseRequest {

    private String title;
    private String description;
    private String language;
}
