package com.example.spring_security.config;

import com.example.spring_security.model.CustomUserDetails;
import com.example.spring_security.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserDetailsService userDetailsService;

    public JWTAuthenticationFilter (JWTService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String tokenHeader = request.getHeader("Authorization");
        if (isTokenPresent(tokenHeader)) {
            String token = extractToken(tokenHeader);
            String username = jwtService.getUsernameFromToken(token);

            if (isValidUser(username)) {
                authenticateUser(username, token, request);
            } else {
                logger.warn("User is not valid or already authenticated for token: {}");
            }
        } else {
            logger.warn("JWT Token is missing in the Authorization header");
        }
        filterChain.doFilter(request, response);
    }

    private boolean isTokenPresent(String tokenHeader) {
        return tokenHeader != null && tokenHeader.startsWith("Bearer ");
    }

    private String extractToken(String tokenHeader) {
        return tokenHeader.substring(7);
    }

    private boolean isValidUser(String username) {
        return username != null && SecurityContextHolder.getContext().getAuthentication() == null;
    }

    private void authenticateUser(String username, String token, HttpServletRequest request) {
        CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);
        if (jwtService.isTokenValidForUser(token, customUserDetails)) {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            customUserDetails,
                            null, // fără parolă pentru că utilizatorul e deja autentificat
                            customUserDetails.getAuthorities()
                    );
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken); // Setează autentificarea în contextul securității
        }
    }
}