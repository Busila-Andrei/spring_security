package com.example.spring_security.config;

import com.example.spring_security.dto.CourseDTO;
import com.example.spring_security.dto.LessonDTO;
import com.example.spring_security.dto.UserDTO;
import com.example.spring_security.model.Course;
import com.example.spring_security.model.Lesson;
import com.example.spring_security.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    private final ModelMapper modelMapper;

    public Mapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public UserDTO toDto(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    public User toEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }


    public LessonDTO toDto(Lesson lesson) {
        return modelMapper.map(lesson, LessonDTO.class);
    }

    public Lesson toEntity(LessonDTO lessonDTO) {
        return modelMapper.map(lessonDTO, Lesson.class);
    }

    public CourseDTO toDto(Course course) {
        return modelMapper.map(course, CourseDTO.class);
    }

    public Course toEntity(CourseDTO courseDTO) {
        return modelMapper.map(courseDTO, Course.class);
    }
}
