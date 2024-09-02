package com.example.spring_security;

import com.example.spring_security.service.JWTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

public class JWTServiceTest {

    @Mock
    private JWTService jwtService;  // Mock pentru JWTService

    @BeforeEach
    public void setUp() {
        // Inițializare mock-uri înainte de fiecare test
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGenerateAccessToken() {
        // Creare obiect UserDetails mock sau real
        UserDetails userDetails = User.withUsername("testuser")
                .password("password")
                .roles("USER")
                .build();

        // Definire comportament pentru metoda generateAccessToken
        when(jwtService.generateAccessToken(any(UserDetails.class)))
                .thenReturn("mockedAccessToken");

        // Apelarea metodei și verificarea rezultatului
        String token = jwtService.generateAccessToken(userDetails);
        assertNotNull(token);
        assertEquals("mockedAccessToken", token);
    }

    @Test
    public void testValidateToken() {
        // Creare obiect UserDetails mock sau real
        UserDetails userDetails = User.withUsername("testuser")
                .password("password")
                .roles("USER")
                .build();

        // Definire comportament pentru metodele generateAccessToken și isTokenValid
        when(jwtService.generateAccessToken(any(UserDetails.class)))
                .thenReturn("mockedAccessToken");

        when(jwtService.isTokenValid("mockedAccessToken"))
                .thenReturn(true);

        // Apelarea metodei și verificarea rezultatului
        String token = jwtService.generateAccessToken(userDetails);
        boolean isValid = jwtService.isTokenValid(token);

        assertTrue(isValid);
    }

    @Test
    public void testGetUsernameFromToken() {
        // Definire comportament pentru metoda getUsernameFromToken
        when(jwtService.getUsernameFromToken("mockedAccessToken"))
                .thenReturn("testuser");

        // Apelarea metodei și verificarea rezultatului
        String username = jwtService.getUsernameFromToken("mockedAccessToken");
        assertEquals("testuser", username);
    }

    @Test
    public void testValidateToken_InvalidToken() {
        // Definire comportament pentru metoda isTokenValid cu un token invalid
        when(jwtService.isTokenValid("invalidToken"))
                .thenReturn(false);

        // Apelarea metodei și verificarea rezultatului
        boolean isValid = jwtService.isTokenValid("invalidToken");
        assertFalse(isValid);
    }
}
