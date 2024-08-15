package com.example.spring_security.repository;

import com.example.spring_security.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    boolean existsByToken(String token);

    Optional<Token> findByToken(String token);
}
