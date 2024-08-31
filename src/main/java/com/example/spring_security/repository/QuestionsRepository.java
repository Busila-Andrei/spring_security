package com.example.spring_security.repository;

import com.example.spring_security.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface QuestionsRepository extends JpaRepository<Question, Long> {

}
