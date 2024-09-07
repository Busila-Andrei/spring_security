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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public ApiResponse<String> createUser(RegisterRequest registerRequest) {
        // Logare început de operație

        // Verificăm dacă utilizatorul există deja
        validateUserDoesNotExist(registerRequest.getEmail());

        // Logare: utilizator inexistent

        // Creăm utilizatorul
        User user = buildUserFromRequest(registerRequest);


        // Creăm și asociem un token de verificare
        Token token = createAndAssignVerificationToken(user);

        // Salvăm utilizatorul și token-ul în baza de date
        userRepository.save(user);

        // Returnăm răspunsul API
        return new ApiResponse<>(
                "User registered successfully with email " + registerRequest.getEmail() +
                        ". Please check your email to confirm your account.",
                token.getToken()
        );
    }

    private void validateUserDoesNotExist(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("User with email " + email + " already exists!");
        }
    }

    private User buildUserFromRequest(RegisterRequest registerRequest) {
        return User.builder()
                .username(registerRequest.getFirstName() + " " + registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                //.password(encryptionService.encryptPassword())
                .role(Role.ROLE_ADMIN)
                .isEmailVerified(false)
                .verificationTokens(new ArrayList<>())
                .build();
    }

    private Token createAndAssignVerificationToken(User user) {
        System.out.println(user.getUsername());
        Token token = createVerificationToken(user);
        System.out.println(token.getToken());
        user.getVerificationTokens().add(token);
        return token;
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
