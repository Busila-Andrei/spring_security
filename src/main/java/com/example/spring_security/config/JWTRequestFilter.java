package com.example.spring_security.config;

import com.example.spring_security.model.CustomUserDetails;
import com.example.spring_security.repository.UserRepository;
import com.example.spring_security.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserRepository userRepository;

    public JWTRequestFilter(JWTService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {;

        System.out.println("--------------------------------------------------------");
        final String tokenHeader = request.getHeader("Authorization");
        System.out.println("tokenHeader: " + tokenHeader);

        if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
            String token = tokenHeader.substring(7);
            System.out.println("Token: " + token);
            if (jwtService.isTokenValid(token)) {
                String username = jwtService.getUsernameFromToken(token);
                System.out.println(username);
                userRepository.findByEmail(username).ifPresent(user -> {
                    CustomUserDetails customUserDetails = new CustomUserDetails(user);
                    System.out.println(customUserDetails.getAuthorities());
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
                    System.out.println(authenticationToken);
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    System.out.println(authenticationToken.getAuthorities() + " " + authenticationToken.getPrincipal() + " " + authenticationToken.getCredentials());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                });
            }
        }
        filterChain.doFilter(request, response);
    }

}
