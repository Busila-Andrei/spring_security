package com.example.spring_security.controller;

import com.example.spring_security.request.CreateExerciseRequest;
import com.example.spring_security.response.ApiResponse;
import com.example.spring_security.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/exercises")
public class ExerciseController {

    private final ExerciseService exerciseService;

    @PostMapping("/create-exercises")
    public ResponseEntity<ApiResponse<?>> createExercise(@RequestBody CreateExerciseRequest request) {
        return ResponseEntity.ok(exerciseService.createexercise(request));
    }

    @GetMapping("/lesson/{lessonId}")
    public ResponseEntity<ApiResponse<?>> getExercisesByLesson(@PathVariable Long lessonId) {
        return ResponseEntity.ok(exerciseService.getExercisesByLesson(lessonId));
    }
}
