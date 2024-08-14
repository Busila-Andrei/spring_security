package com.example.spring_security.controller;

import com.example.spring_security.dto.UserDTO;
import com.example.spring_security.exception.UnauthorizedException;
import com.example.spring_security.exception.UserAlreadyExistsException;
import com.example.spring_security.exception.UserNotFoundException;
import com.example.spring_security.model.User;
import com.example.spring_security.request.LoginRequest;
import com.example.spring_security.request.RegisterRequest;
import com.example.spring_security.response.ApiResponse;
import com.example.spring_security.service.JWTService;
import com.example.spring_security.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/")
public class UserController {

    private final UserService userService;
    private final JWTService jwtService;

    //@GetMapping("/{userID}/user")


    @PostMapping("/auth/register")
    public ResponseEntity<ApiResponse> createUser(@RequestBody @Valid RegisterRequest registerRequest) {
        try {
            userService.createUser(registerRequest);
            return ResponseEntity.ok(new ApiResponse("User registered successfully. Please check your email to confirm your account.", null));
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    //@GetMapping("/auth/confirm?token={confirmationToken}")
    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse> loginUser(@RequestBody @Valid LoginRequest loginRequest) {
        try {
            User user = userService.loginUser(loginRequest);
            String token = jwtService.generateJWT(user);
            return ResponseEntity.ok(new ApiResponse("User details retrieved successfully.", token));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
    //@PostMapping("/auth/refresh-token")
    //@PostMapping("/auth/forgot-password")
    //@PostMapping("/auth/reset-password")
    //@PostMapping("/auth/change-password")
    @GetMapping("/auth/{userEmail}/me")
    public ResponseEntity<ApiResponse> getUserByEmail(@PathVariable String userEmail) {
        try {
            User user = userService.getUserByEmail(userEmail);
            UserDTO userDTO = userService.convertUserToUserDTO(user);
            return ResponseEntity.ok(new ApiResponse("User details retrieved successfully.", userDTO));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
        }
    }
    //@PutMapping("/user/me")
    //@PostMapping("/auth/logout")
    //@GetMapping("/auth/validate-token")
    //@PostMapping("/auth/resend-confirmation")



}
