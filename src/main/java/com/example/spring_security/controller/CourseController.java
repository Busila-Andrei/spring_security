package com.example.spring_security.controller;


import com.example.spring_security.request.CreateCourseRequest;
import com.example.spring_security.response.ApiResponse;
import com.example.spring_security.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/courses/")
public class CourseController {

    private final CourseService courseService;

    @PostMapping("/create-course")
    public ResponseEntity<ApiResponse<?>> createCourse(@RequestBody CreateCourseRequest request) {
        return ResponseEntity.ok(courseService.createCourse(request));
    }

    @GetMapping("/get-courses")
    public ResponseEntity<ApiResponse<?>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @PostMapping("/{courseId}/enroll/{userId}")
    public ResponseEntity<ApiResponse<?>> enrollUserToCourse(@PathVariable Long courseId, @PathVariable Long userId) {
        return ResponseEntity.ok(courseService.enrollUserToCourse(userId, courseId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<?>> getCoursesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(courseService.getCoursesByUser(userId));
    }
}
