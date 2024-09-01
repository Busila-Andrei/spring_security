package com.example.spring_security.service;


import com.example.spring_security.exception.UserAlreadyExistsException;
import com.example.spring_security.model.Course;
import com.example.spring_security.model.Lesson;
import com.example.spring_security.repository.CourseRepository;
import com.example.spring_security.repository.LessonRepository;
import com.example.spring_security.request.CreateLessonRequest;
import com.example.spring_security.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;

    public ApiResponse<Lesson> createLesson(CreateLessonRequest request) {
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(
                () -> new RuntimeException("Course not found")
        );

        if (lessonRepository.existsByCode(request.getCode())) {
            throw new UserAlreadyExistsException("Lesson with code " + request.getCode() + " already exists!");
        }

        Lesson lesson = Lesson.builder()
                .code(request.getCode())
                .title(request.getTitle())
                .description(request.getDescription())
                .course(course).build();
    lessonRepository.save(lesson);
    return new ApiResponse<>("Lesson created successfully", lesson);
    }

    public ApiResponse<List<Lesson>> getLessonsByCourse(Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new RuntimeException("Course not found")
        );

        return new ApiResponse<>("Lessons retrieved successfully", course.getLessons());

    }
}
