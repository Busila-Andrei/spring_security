package com.example.spring_security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private String bearerToken;

    @BeforeEach
    public void setUp() {
        // Simulate getting a JWT token, or hard-code one here if needed.
        bearerToken = "your-jwt-token";
    }

    @Test
    public void testApplicationHealth() throws Exception {
        mockMvc.perform(get("/actuator/health")
                        .header("Authorization", "Bearer " + bearerToken))
                .andExpect(status().isOk());
    }
}

