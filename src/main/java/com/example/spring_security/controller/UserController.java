package com.example.spring_security.controller;

import com.example.spring_security.exception.UserNotFoundException;
import com.example.spring_security.model.User;
import com.example.spring_security.repository.UserRepository;
import com.example.spring_security.request.LoginRequest;
import com.example.spring_security.request.RegisterRequest;
import com.example.spring_security.response.ApiResponse;
import com.example.spring_security.service.JWTService;
import com.example.spring_security.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/")
public class UserController {
    private final UserRepository userRepository;


    //@GetMapping("/{userID}/user")


    private final UserService userService;
    private final JWTService jwtService;

    @PostMapping("/auth/register")
    public ResponseEntity<ApiResponse<String>> registerUser(@RequestBody @Valid RegisterRequest registerRequest) {
        ApiResponse<String> apiResponse = userService.createUser(registerRequest);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/auth/validate-token")
    public ResponseEntity<ApiResponse<String>> validateTokenAndEnableUser(@RequestParam("token") String token) {
        ApiResponse<String> apiResponse = userService.validateTokenAndEnableUser(token);
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse<Map<String, String>>> loginUser(@RequestBody @Valid LoginRequest loginRequest) {
        ApiResponse<Map<String, String>> apiResponse = userService.loginUser(loginRequest);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/auth/me")
    public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findUserByEmail(userDetails.getUsername());
        return ResponseEntity.ok(user);
    }


    //TODO LIST
    //@PostMapping("/auth/refresh-token")
    //@PostMapping("/auth/forgot-password")
    //@PostMapping("/auth/reset-password")
    //@PostMapping("/auth/change-password")
    //@GetMapping("/auth/{userEmail}/me")
    //public ResponseEntity<ApiResponse> getUserByEmail(@PathVariable String userEmail) {}
    //@PutMapping("/user/me")
    //@PostMapping("/auth/logout")
    //@PostMapping("/auth/resend-confirmation")



}
