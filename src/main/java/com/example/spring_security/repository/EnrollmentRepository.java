package com.example.spring_security.repository;

import com.example.spring_security.model.Enrollment;
import org.springframework.data.repository.CrudRepository;

public interface EnrollmentRepository extends CrudRepository<Enrollment, Long> {

}
