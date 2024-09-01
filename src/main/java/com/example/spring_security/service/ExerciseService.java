package com.example.spring_security.service;

import com.example.spring_security.model.Exercise;
import com.example.spring_security.model.Lesson;
import com.example.spring_security.repository.ExerciseRepository;
import com.example.spring_security.repository.LessonRepository;
import com.example.spring_security.request.CreateExerciseRequest;
import com.example.spring_security.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final LessonRepository lessonRepository;

    public ApiResponse<Exercise> createExercise(CreateExerciseRequest request) {
        Lesson lesson = lessonRepository.findById(request.getLessonId()).orElseThrow(
                () -> new RuntimeException("Lesson not found")
        );

        Exercise exercise = Exercise.builder()
                .question(request.getQuestion())
                .answer(request.getAnswer())
                .lesson(lesson)
                .build();

        exerciseRepository.save(exercise);
        return new ApiResponse<>("Exercise created successfully", exercise);
    }

    public ApiResponse<List<Exercise>> getExercisesByLesson(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(
                () -> new RuntimeException("Lesson not found")
        );

        return new ApiResponse<>("Exercises retrieved successfully", lesson.getExercises());
    }
}
