package com.example.spring_security.service;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final EncryptionService encryptionService;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    public ApiResponse<String> createUser(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new UserAlreadyExistsException("User with email " + registerRequest.getEmail() + " already exists!");
        }

        User user = new User();

        user.setUsername(registerRequest.getFirstName() + " " + registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encryptionService.encryptPassword(registerRequest.getPassword()));
        user.setRole(Role.ROLE_ADMIN);
        user.setIsEmailVerified(false);

        Token token = createVerificationToken(user);
        user.getVerificationTokens().add(token);
        userRepository.save(user);
        System.out.println(token.getToken());

        logger.debug("Generated verification token: {}", token.getToken());
        return new ApiResponse<>("User registered successfully with email " + registerRequest.getEmail() + ". Please check your email to confirm your account.", token.getToken());
    }


    public Token createVerificationToken(User user) {
        Token token = new Token();
        String generatedToken = jwtService.generateAccessToken(new CustomUserDetails(user));
        token.setToken(generatedToken);
        token.setCreatedTimestamp(jwtService.getIssuedAtDateFromToken(generatedToken));
        token.setUser(user);
        return token;
    }

    public ApiResponse<Map<String, String>> loginUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword())
        );


        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        Map<String, String> response = new HashMap<>();
        response.put("accessToken", accessToken);
        response.put("refreshToken", refreshToken);
        System.out.println(accessToken);
        return new ApiResponse<>("Login successful.", response);
    }

    public ApiResponse<String> validateTokenAndEnableUser(String token) {
        if (jwtService.isTokenValid(token)) {
            String username = jwtService.getUsernameFromToken(token);
            User user = findUserByEmail(username);

            if (!user.getIsEmailVerified()) {
                user.setIsEmailVerified(true);
                userRepository.save(user);
                return new ApiResponse<>("User account has been successfully enabled.");
            } else {
                return new ApiResponse<>("User account is already enabled.");
            }
        } else {
            return new ApiResponse<>("Invalid or expired token.");
        }
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }
}
