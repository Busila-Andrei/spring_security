package com.example.spring_security.controller;

import com.example.spring_security.dto.UserDTO;
import com.example.spring_security.model.User;
import com.example.spring_security.request.LoginRequest;
import com.example.spring_security.request.RegisterRequest;
import com.example.spring_security.response.ApiResponse;
import com.example.spring_security.service.JWTService;
import com.example.spring_security.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/")
public class UserController {

    private final UserService userService;
    private final JWTService jwtService;

    //@GetMapping("/{userID}/user")


    @PostMapping("/auth/register")
    public ResponseEntity<ApiResponse<UserDTO>> registerUser(@RequestBody @Valid RegisterRequest registerRequest) {
        UserDTO userDTO = userService.createUser(registerRequest);
        return ResponseEntity.ok(new ApiResponse<>("User registered successfully. Please check your email to confirm your account.", userDTO));
    }

    //@GetMapping("/auth/confirm?token={confirmationToken}")
    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse<String>> loginUser(@RequestBody @Valid LoginRequest loginRequest) {
            UserDTO userDTO = userService.loginUser(loginRequest);
            String token = jwtService.generateJWT(userDTO);
            return ResponseEntity.ok(new ApiResponse<>("User details retrieved successfully.", token));
    }

    @GetMapping("/me")
    public User getLoggedInUserProfile(@AuthenticationPrincipal User user) {
        return user;
    }

    //@PostMapping("/auth/refresh-token")
    //@PostMapping("/auth/forgot-password")
    //@PostMapping("/auth/reset-password")
    //@PostMapping("/auth/change-password")
//    @GetMapping("/auth/{userEmail}/me")
//    public ResponseEntity<ApiResponse> getUserByEmail(@PathVariable String userEmail) {
//        try {
//            User user = userService.getUserByEmail(userEmail);
//            UserDTO userDTO = userService.convertUserToUserDTO(user);
//            return ResponseEntity.ok(new ApiResponse("User details retrieved successfully.", userDTO));
//        } catch (UserNotFoundException e) {
//            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
//        } catch (UnauthorizedException e) {
//            return ResponseEntity.status(UNAUTHORIZED).body(new ApiResponse(e.getMessage(), null));
//        }
//    }
    //@PutMapping("/user/me")
    //@PostMapping("/auth/logout")
    //@GetMapping("/auth/validate-token")
    //@PostMapping("/auth/resend-confirmation")



}
