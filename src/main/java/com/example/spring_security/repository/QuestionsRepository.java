package com.example.spring_security.repository;

import com.example.spring_security.model.Question;
import org.springframework.data.repository.CrudRepository;

public interface QuestionsRepository extends CrudRepository<Question, Long> {

}
