package com.example.spring_security.controller;

import com.example.spring_security.model.User;
import com.example.spring_security.request.LoginRequest;
import com.example.spring_security.request.RegisterRequest;
import com.example.spring_security.response.ApiResponse;
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


    //@GetMapping("/{userID}/user")


    @PostMapping("/auth/register")
    public ResponseEntity<ApiResponse<String>> registerUser(@RequestBody @Valid RegisterRequest registerRequest) {
        ApiResponse<String> apiResponse = userService.createUser(registerRequest);
        return ResponseEntity.ok(apiResponse);
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
