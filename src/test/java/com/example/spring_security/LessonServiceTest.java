package com.example.spring_security;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.spring_security.repository.LessonRepository;
import com.example.spring_security.repository.CourseRepository;
import com.example.spring_security.model.Course;
import com.example.spring_security.model.Lesson;
import com.example.spring_security.service.LessonService;
import com.example.spring_security.dto.LessonDTO;
import com.example.spring_security.request.CreateLessonRequest;
import com.example.spring_security.response.ApiResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class LessonServiceTest {

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private LessonService lessonService;

    @Test
    public void testCreateLesson_Success() {
        CreateLessonRequest request = new CreateLessonRequest();
        request.setCode("L001");
        request.setTitle("Lesson 1");
        request.setDescription("Introduction to course");
        request.setCourseId(1L);

        Course course = new Course();
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));
        when(lessonRepository.existsByCode(anyString())).thenReturn(false);

        ApiResponse<LessonDTO> response = lessonService.createLesson(request);

        assertEquals("Lesson created successfully", response.getMessage());
        verify(lessonRepository, times(1)).save(any(Lesson.class));
    }

    @Test
    public void testCreateLesson_CourseNotFound() {
        CreateLessonRequest request = new CreateLessonRequest();
        request.setCourseId(1L);

        when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                lessonService.createLesson(request));

        assertEquals("Course not found", exception.getMessage());
    }

    @Test
    public void testGetLessonsByCourse_Success() {
        Course course = new Course();
        Lesson lesson = new Lesson();
        lesson.setCode("L001");
        course.setLessons(List.of(lesson));

        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));

        ApiResponse<List<Lesson>> response = lessonService.getLessonsByCourse(1L);

        assertEquals(1, response.getData().size());
        assertEquals("L001", response.getData().get(0).getCode());
    }

    @Test
    public void testGetLessonsByCourse_CourseNotFound() {
        when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                lessonService.getLessonsByCourse(1L));

        assertEquals("Course not found", exception.getMessage());
    }
}
