package com.example.spring_security.service;


import com.example.spring_security.config.UserMapper;
import com.example.spring_security.dto.UserDTO;
import com.example.spring_security.exception.UnauthorizedException;
import com.example.spring_security.exception.UserAlreadyExistsException;
import com.example.spring_security.exception.UserNotFoundException;
import com.example.spring_security.model.CustomUserDetails;
import com.example.spring_security.model.Role;
import com.example.spring_security.model.Token;
import com.example.spring_security.model.User;
import com.example.spring_security.repository.UserRepository;
import com.example.spring_security.request.LoginRequest;
import com.example.spring_security.request.RegisterRequest;
import com.example.spring_security.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EncryptionService encryptionService;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;



    public ApiResponse<String> createUser(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new UserAlreadyExistsException("User with email " + registerRequest.getEmail() + " already exists!");
        }

        // Create a new user and set its properties
        User user = new User();
        user.setUsername(registerRequest.getFirstName() + " " + registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encryptionService.encryptPassword(registerRequest.getPassword()));
        user.setRole(Role.USER);
        user.setIsEmailVerified(false);

        Token token = tokenService.generateAccessToken(user);
        user.getVerificationTokens().add(token);
        userRepository.save(user);
        System.out.println(token.getToken());
        return new ApiResponse<>("User registered successfully. Please check your email to confirm your account.");
    }


    public ApiResponse<Map<String, String>> loginUser(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword())
        );

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + loginRequest.getEmail()));

        if (!encryptionService.verifyPassword(loginRequest.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials.");
        }

        String accessToken = jwtService.generateAccessToken(new CustomUserDetails(user));
        String refreshToken = jwtService.generateRefreshToken(new CustomUserDetails(user));

        Map<String, String> response = new HashMap<>();
        response.put("accessToken", accessToken);
        response.put("refreshToken", refreshToken);
        return new ApiResponse<>("Login successful.", response);
    }



}
