package com.example.spring_security.repository;

import com.example.spring_security.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

    boolean existsByCode(String code);
}
