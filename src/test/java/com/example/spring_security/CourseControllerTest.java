package com.example.spring_security;

import com.example.spring_security.controller.CourseController;
import com.example.spring_security.request.CreateCourseRequest;
import com.example.spring_security.response.ApiResponse;
import com.example.spring_security.service.CourseService;
import com.example.spring_security.service.JWTService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CourseController.class)
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @MockBean
    private JWTService jwtService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testCreateCourse() throws Exception {
        CreateCourseRequest request = new CreateCourseRequest();
        request.setCode("C001");
        request.setTitle("English Course");
        request.setDescription("Basic English Course");
        request.setLanguage("English");

        when(courseService.createCourse(any(CreateCourseRequest.class)))
                .thenReturn(new ApiResponse<>("Course created successfully.", null));

        mockMvc.perform(post("/api/courses/create-course")
                        .with(csrf())  // Include CSRF token
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"C001\",\"title\":\"English Course\",\"description\":\"Basic English Course\",\"language\":\"English\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Course created successfully."));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetAllCourses() throws Exception {
        when(courseService.getAllCourses())
                .thenReturn(new ApiResponse<>("Courses retrieved successfully", List.of()));

        mockMvc.perform(get("/api/courses/get-courses")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Courses retrieved successfully"));
    }
}
