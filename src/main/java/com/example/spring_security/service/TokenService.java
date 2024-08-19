package com.example.spring_security.service;


import com.example.spring_security.exception.UserAlreadyExistsException;
import com.example.spring_security.model.CustomUserDetails;
import com.example.spring_security.model.Token;
import com.example.spring_security.model.User;
import com.example.spring_security.repository.TokenRepository;
import com.example.spring_security.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;
    private final JWTService jwtService;


    public String generateAccessToken(User user) {
        String token = jwtService.generateAccessToken(new CustomUserDetails(user));
        if (tokenRepository.existsByToken(token)) {
            throw new UserAlreadyExistsException("Token " + token +" already exists!");
        }
        Token tokenEntity = new Token();
        tokenEntity.setToken(token);
        tokenEntity.setCreatedTimestamp(jwtService.getIssuedAtDateFromToken(token));
        tokenEntity.setUser(user);
        tokenRepository.save(tokenEntity);
        return token;

    }
}
