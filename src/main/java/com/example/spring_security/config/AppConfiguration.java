package com.example.spring_security.config;

import com.example.spring_security.service.JWTService;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public JWTService jwtService(JwtConfig jwtConfig) {
        return new JWTService(jwtConfig);
    }
}
