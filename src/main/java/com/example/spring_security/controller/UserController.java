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
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/")
public class UserController {

    private final UserService userService;
    private final JWTService jwtService;
    private final UserRepository userRepository;


    //@GetMapping("/{userID}/user")


    @PostMapping("/auth/register")
    public ResponseEntity<ApiResponse<String>> registerUser(@RequestBody @Valid RegisterRequest registerRequest) {
        ApiResponse<String> apiResponse = userService.createUser(registerRequest);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/auth/validate-token")
    public ResponseEntity<ApiResponse<String>> validateTokenAndEnableUser(@RequestParam("token") String token) {
        if (jwtService.isTokenValid(token)) {
            String username = jwtService.getUsernameFromToken(token);
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new UserNotFoundException("User not found with email: " + username));

            if (!user.getIsEmailVerified()) {
                user.setIsEmailVerified(true);  // Setează utilizatorul ca fiind activat
                userRepository.save(user);  // Salvează modificările în baza de date
                return ResponseEntity.ok(new ApiResponse<>("User account has been successfully enabled."));
            } else {
                return ResponseEntity.badRequest().body(new ApiResponse<>("User account is already enabled."));
            }
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse<>("Invalid or expired token."));
        }
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse<Map<String, String>>> loginUser(@RequestBody @Valid LoginRequest loginRequest) {
        ApiResponse<Map<String, String>> apiResponse = userService.loginUser(loginRequest);
        return ResponseEntity.ok(apiResponse);

    }

    @GetMapping("/me")
    public User getLoggedInUserProfile(@AuthenticationPrincipal User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication); //get all details(name,email,password,roles e.t.c) of the user
        System.out.println(authentication.getDetails()); // get remote ip
        System.out.println(authentication.getName());
        return user;
    }

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
