package com.example.spring_security;

import com.example.spring_security.controller.LessonController;
import com.example.spring_security.request.CreateLessonRequest;
import com.example.spring_security.response.ApiResponse;
import com.example.spring_security.service.LessonService;
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

@WebMvcTest(LessonController.class)
public class LessonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LessonService lessonService;

    @Test
    public void testCreateLesson() throws Exception {
        CreateLessonRequest request = new CreateLessonRequest();
        request.setCode("L001");
        request.setTitle("Lesson 1");
        request.setDescription("Introduction to course");
        request.setCourseId(1L);

        when(lessonService.createLesson(any(CreateLessonRequest.class)))
                .thenReturn(new ApiResponse<>("Lesson created successfully", null));

        mockMvc.perform(post("/api/lessons/create-lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"L001\",\"title\":\"Lesson 1\",\"description\":\"Introduction to course\",\"courseId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Lesson created successfully."));
    }

    @Test
    public void testGetLessonsByCourse() throws Exception {
        when(lessonService.getLessonsByCourse(anyLong()))
                .thenReturn(new ApiResponse<>("Lessons retrieved successfully", List.of()));

        mockMvc.perform(get("/api/lessons/course/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Lessons retrieved successfully"));
    }
}

