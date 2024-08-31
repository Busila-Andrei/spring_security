package com.example.spring_security.service;

import com.example.spring_security.model.Course;
import com.example.spring_security.model.Question;
import com.example.spring_security.repository.CourseRepository;
import com.example.spring_security.repository.QuestionsRepository;
import com.example.spring_security.request.CreateQuestionRequest;
import com.example.spring_security.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionsRepository questionsRepository;
    private final CourseRepository courseRepository;

    public ApiResponse<Question> createQuestion(CreateQuestionRequest request) {
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow(
                () -> new RuntimeException("Course not found")
        );

        Question question = Question.builder()
                .content(request.getContent())
                .options(request.getOptions())
                .correctAnswer(request.getCorrectAnswer())
                .course(course)
                .build();

        questionsRepository.save(question);
        return new ApiResponse<>("Question created successfully", question);
    }

    public ApiResponse<List<Question>> getQuestionByCourse(Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(
                () -> new RuntimeException("Course not found")
        );

        return new ApiResponse<>("Questions retrieved successfully", course.getQuestions());
    }
}
