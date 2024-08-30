package com.example.spring_security.service;


import com.example.spring_security.model.Course;
import com.example.spring_security.repository.CourseRepository;
import com.example.spring_security.request.CreateCourseRequest;
import com.example.spring_security.response.ApiResponse;
import jakarta.validation.constraints.Negative;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    public ApiResponse<Course> createCourse(CreateCourseRequest request) {
        Course course = Course.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .language(request.getLanguage())
                .build();

        courseRepository.save(course);
        return new ApiResponse<>("Course created successfully.", course);
    }

    public ApiResponse<List<Course>> getAllCourses() {
        return new ApiResponse<>("Courses retrieved successfully", courseRepository.findAll());
    }
}
