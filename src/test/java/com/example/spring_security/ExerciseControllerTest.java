package com.example.spring_security;

import com.example.spring_security.controller.ExerciseController;
import com.example.spring_security.request.CreateExerciseRequest;
import com.example.spring_security.response.ApiResponse;
import com.example.spring_security.service.ExerciseService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExerciseController.class)
public class ExerciseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExerciseService exerciseService;

    @Test
    public void testCreateExercise() throws Exception {
        CreateExerciseRequest request = new CreateExerciseRequest();
        request.setQuestion("What is the capital of France?");
        request.setAnswer("Paris");
        request.setLessonId(1L);

        when(exerciseService.createExercise(any(CreateExerciseRequest.class)))
                .thenReturn(new ApiResponse<>("Exercise created successfully", null));

        mockMvc.perform(post("/api/exercises/create-exercises")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"question\":\"What is the capital of France?\",\"answer\":\"Paris\",\"lessonId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Exercise created successfully."));
    }

    @Test
    public void testGetExercisesByLesson() throws Exception {
        when(exerciseService.getExercisesByLesson(anyLong()))
                .thenReturn(new ApiResponse<>("Exercises retrieved successfully", List.of()));

        mockMvc.perform(get("/api/exercises/lesson/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Exercises retrieved successfully"));
    }
}

