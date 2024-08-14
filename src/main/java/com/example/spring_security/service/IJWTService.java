package com.example.spring_security.service;

import com.example.spring_security.model.User;

public interface IJWTService {

    String generateJWT(User user);

}
