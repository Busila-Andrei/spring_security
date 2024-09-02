package com.example.spring_security;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.spring_security.repository.ExerciseRepository;
import com.example.spring_security.repository.LessonRepository;
import com.example.spring_security.model.Exercise;
import com.example.spring_security.model.Lesson;
import com.example.spring_security.service.ExerciseService;
import com.example.spring_security.request.CreateExerciseRequest;
import com.example.spring_security.response.ApiResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ExerciseServiceTest {

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private LessonRepository lessonRepository;

    @InjectMocks
    private ExerciseService exerciseService;

    @Test
    public void testCreateExercise_Success() {
        CreateExerciseRequest request = new CreateExerciseRequest();
        request.setQuestion("What is the capital of France?");
        request.setAnswer("Paris");
        request.setLessonId(1L);

        Lesson lesson = new Lesson();
        when(lessonRepository.findById(anyLong())).thenReturn(Optional.of(lesson));

        ApiResponse<Exercise> response = exerciseService.createExercise(request);

        assertEquals("Exercise created successfully", response.getMessage());
        verify(exerciseRepository, times(1)).save(any(Exercise.class));
    }

    @Test
    public void testCreateExercise_LessonNotFound() {
        CreateExerciseRequest request = new CreateExerciseRequest();
        request.setLessonId(1L);

        when(lessonRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                exerciseService.createExercise(request));

        assertEquals("Lesson not found", exception.getMessage());
    }

    @Test
    public void testGetExercisesByLesson_Success() {
        Lesson lesson = new Lesson();
        Exercise exercise = new Exercise();
        exercise.setQuestion("What is the capital of France?");
        lesson.setExercises(List.of(exercise));

        when(lessonRepository.findById(anyLong())).thenReturn(Optional.of(lesson));

        ApiResponse<List<Exercise>> response = exerciseService.getExercisesByLesson(1L);

        assertEquals(1, response.getData().size());
        assertEquals("What is the capital of France?", response.getData().get(0).getQuestion());
    }

    @Test
    public void testGetExercisesByLesson_LessonNotFound() {
        when(lessonRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                exerciseService.getExercisesByLesson(1L));

        assertEquals("Lesson not found", exception.getMessage());
    }
}
