package com.example.spring_security.repository;

import com.example.spring_security.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
}
