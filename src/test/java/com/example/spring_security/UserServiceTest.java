package com.example.spring_security;



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
import com.example.spring_security.service.EncryptionService;
import com.example.spring_security.service.JWTService;
import com.example.spring_security.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EncryptionService encryptionService;

    @Mock
    private JWTService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_ShouldThrowException_WhenUserAlreadyExists() {
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest("John", "Doe", "john.doe@example.com", "password");
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(registerRequest));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void createUser_ShouldCreateNewUser_WhenUserDoesNotExist() {
        // Arrange
        RegisterRequest registerRequest = new RegisterRequest("John", "Doe", "john.doe@example.com", "password");
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(encryptionService.encryptPassword(anyString())).thenReturn("encryptedPassword");
        when(jwtService.generateAccessToken(any())).thenReturn("verificationToken");

        // Act
        ApiResponse<String> response = userService.createUser(registerRequest);

        // Assert
        assertNotNull(response);
        assertEquals("User registered successfully with email john.doe@example.com. Please check your email to confirm your account.", response.getMessage());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void loginUser_ShouldReturnTokens_WhenAuthenticationIsSuccessful() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("john.doe@example.com", "password");
        Authentication authentication = mock(Authentication.class);
        CustomUserDetails userDetails = mock(CustomUserDetails.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtService.generateAccessToken(any())).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(any())).thenReturn("refreshToken");

        // Act
        ApiResponse<Map<String, String>> response = userService.loginUser(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals("Login successful.", response.getMessage());
        assertEquals("accessToken", response.getData().get("accessToken"));
        assertEquals("refreshToken", response.getData().get("refreshToken"));
    }

    @Test
    void validateTokenAndEnableUser_ShouldEnableUser_WhenTokenIsValid() {
        // Arrange
        String token = "validToken";
        User user = new User();
        user.setEmail("john.doe@example.com");
        user.setIsEmailVerified(false);

        when(jwtService.isTokenValid(anyString())).thenReturn(true);
        when(jwtService.getUsernameFromToken(anyString())).thenReturn("john.doe@example.com");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        // Act
        ApiResponse<String> response = userService.validateTokenAndEnableUser(token);

        // Assert
        assertNotNull(response);
        assertEquals("User account has been successfully enabled.", response.getMessage());
        assertTrue(user.getIsEmailVerified());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void validateTokenAndEnableUser_ShouldReturnAlreadyEnabled_WhenUserIsAlreadyEnabled() {
        // Arrange
        String token = "validToken";
        User user = new User();
        user.setEmail("john.doe@example.com");
        user.setIsEmailVerified(true);

        when(jwtService.isTokenValid(anyString())).thenReturn(true);
        when(jwtService.getUsernameFromToken(anyString())).thenReturn("john.doe@example.com");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        // Act
        ApiResponse<String> response = userService.validateTokenAndEnableUser(token);

        // Assert
        assertNotNull(response);
        assertEquals("User account is already enabled.", response.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void findUserByEmail_ShouldReturnUser_WhenUserExists() {
        // Arrange
        User user = new User();
        user.setEmail("john.doe@example.com");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        // Act
        User foundUser = userService.findUserByEmail("john.doe@example.com");

        // Assert
        assertNotNull(foundUser);
        assertEquals("john.doe@example.com", foundUser.getEmail());
    }

    @Test
    void findUserByEmail_ShouldThrowException_WhenUserDoesNotExist() {
        // Arrange
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.findUserByEmail("nonexistent@example.com"));
    }
}
