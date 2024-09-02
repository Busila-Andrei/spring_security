package com.example.spring_security.repository;

import com.example.spring_security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByUsernameIgnoreCase(String username);

    Optional<User> findByEmail(String email);

    int countAllByEmailContaining(String email);

}
