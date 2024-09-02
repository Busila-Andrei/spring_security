package com.example.spring_security;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.spring_security.config.Mapper;
import com.example.spring_security.repository.CourseRepository;
import com.example.spring_security.repository.EnrollmentRepository;
import com.example.spring_security.repository.UserRepository;
import com.example.spring_security.model.Course;
import com.example.spring_security.model.User;
import com.example.spring_security.model.Enrollment;
import com.example.spring_security.service.CourseService;
import com.example.spring_security.dto.CourseDTO;
import com.example.spring_security.request.CreateCourseRequest;
import com.example.spring_security.response.ApiResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    private CourseService courseService;

    @Mock
    private Mapper mapper;

    @Test
    public void testCreateCourse_Success() {
        // Create a mock CreateCourseRequest
        CreateCourseRequest request = new CreateCourseRequest();
        request.setTitle("Test Course");
        request.setDescription("Test Description");
        // Set other necessary fields on the request

        // Create a mock Course and CourseDto
        Course course = new Course();
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());

        CourseDTO courseDto = new CourseDTO();
        courseDto.setTitle(request.getTitle());
        courseDto.setDescription(request.getDescription());

        // Configure the mock behavior
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        when(mapper.toDto(any(Course.class))).thenReturn(courseDto);

        // Call the method under test
        CourseDTO result = courseService.createCourse(request).getData();

        // Assertions
        assertNotNull(result);
        assertEquals(courseDto, result);
        assertEquals(request.getTitle(), result.getTitle());
        assertEquals(request.getDescription(), result.getDescription());
    }

    @Test
    public void testCreateCourse_CourseAlreadyExists() {
        CreateCourseRequest request = new CreateCourseRequest();
        request.setCode("C001");

        when(courseRepository.existsByCode(anyString())).thenReturn(true);

        Exception exception = assertThrows(RuntimeException.class, () -> courseService.createCourse(request));

        assertEquals("Course with code C001 already exists!", exception.getMessage());
        verify(courseRepository, times(0)).save(any(Course.class));
    }

    @Test
    public void testGetAllCourses() {
        Course course = new Course();
        course.setCode("C001");

        when(courseRepository.findAll()).thenReturn(List.of(course));

        ApiResponse<List<Course>> response = courseService.getAllCourses();

        assertEquals(1, response.getData().size());
        assertEquals("C001", response.getData().get(0).getCode());
    }

    @Test
    public void testEnrollUserToCourse_UserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> courseService.enrollUserToCourse(1L, 1L));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    public void testEnrollUserToCourse_CourseNotFound() {
        User user = new User();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> courseService.enrollUserToCourse(1L, 1L));

        assertEquals("Course not found", exception.getMessage());
    }

    @Test
    public void testEnrollUserToCourse_Success() {
        User user = new User();
        Course course = new Course();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));

        ApiResponse<String> response = courseService.enrollUserToCourse(1L, 1L);

        assertEquals("User enrolled successfully to the course.", response.getMessage());
        verify(enrollmentRepository, times(1)).save(any(Enrollment.class));
    }

    @Test
    public void testGetCoursesByUser() {
        Enrollment enrollment = new Enrollment();
        Course course = new Course();
        course.setCode("C001");
        enrollment.setCourse(course);

        when(enrollmentRepository.findByUserId(anyLong())).thenReturn(List.of(enrollment));

        ApiResponse<List<Course>> response = courseService.getCoursesByUser(1L);

        assertEquals(1, response.getData().size());
        assertEquals("C001", response.getData().get(0).getCode());
    }
}
