package com.example.spring_security.repository;

import com.example.spring_security.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {


}
