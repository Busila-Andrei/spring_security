package com.example.spring_security.controller;

import com.example.spring_security.dto.UserDTO;
import com.example.spring_security.exception.UserAlreadyExistsException;
import com.example.spring_security.model.User;
import com.example.spring_security.request.RegisterRequest;
import com.example.spring_security.response.ApiResponse;
import com.example.spring_security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CONFLICT;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/auth/")
public class UserController {

    private final UserService userService;

    //@GetMapping("/{userID}/user")


    @PostMapping("/register")
    public ResponseEntity<ApiResponse> createUser(@RequestBody RegisterRequest registerRequest) {
        try {
            User user = userService.createUser(registerRequest);
            UserDTO userDTO = userService.convertUserToUserDTO(user);
            return ResponseEntity.ok(new ApiResponse("User registered successfully. Please check your email to confirm your account.", userDTO));
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
        }
    }

    //@GetMapping("/auth/confirm?token={confirmationToken}")
    //@PostMapping("/auth/login")
    //@PostMapping("/auth/refresh-token")
    //@PostMapping("/auth/forgot-password")
    //@PostMapping("/auth/reset-password")
    //@PostMapping("/auth/change-password")
    //@GetMapping("/user/me")
    //@PutMapping("/user/me")
    //@PostMapping("/auth/logout")
    //@GetMapping("/auth/validate-token")
    //@PostMapping("/auth/resend-confirmation")



}
