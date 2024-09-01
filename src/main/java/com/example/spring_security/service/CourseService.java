package com.example.spring_security.service;


import com.example.spring_security.config.Mapper;
import com.example.spring_security.dto.CourseDTO;
import com.example.spring_security.exception.UserAlreadyExistsException;
import com.example.spring_security.model.Course;
import com.example.spring_security.model.Enrollment;
import com.example.spring_security.model.User;
import com.example.spring_security.repository.CourseRepository;
import com.example.spring_security.repository.EnrollmentRepository;
import com.example.spring_security.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final Mapper mapper;

    public ApiResponse<CourseDTO> createCourse(CreateCourseRequest request) {

        if (courseRepository.existsByCode(request.getCode())) {
            throw new UserAlreadyExistsException("Course with code " + request.getCode() + " already exists!");
        }
        Course course = Course.builder()
                .code(request.getCode())
                .title(request.getTitle())
                .description(request.getDescription())
                .language(request.getLanguage())
                .build();

        courseRepository.save(course);
        return new ApiResponse<>("Course created successfully.", mapper.toDto(course));
    }

    public ApiResponse<List<Course>> getAllCourses() {
        return new ApiResponse<>("Courses retrieved successfully", courseRepository.findAll());
    }

    public ApiResponse<String> enrollUserToCourse(Long userId, Long courseId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new RuntimeException("Course not found")
        );

        Enrollment enrollment = Enrollment.builder()
                .user(user)
                .course(course)
                .build();
        enrollmentRepository.save(enrollment);
        return new ApiResponse<>("User enrolled successfully to the course.");
    }

    public ApiResponse<List<Course>> getCoursesByUser(Long userId) {
        List<Enrollment> enrollments = enrollmentRepository.findByUserId(userId);
        List<Course> courses = enrollments.stream().map(Enrollment::getCourse).toList();
        return new ApiResponse<>("Courses retrieved successfully for the user.", courses);
    }


}
