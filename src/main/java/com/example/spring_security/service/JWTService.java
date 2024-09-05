package com.example.spring_security.service;

import com.example.spring_security.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

    private final JwtConfig jwtConfig;

    public JWTService(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @PostConstruct
    public void init() {
        byte[] keyBytes = jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8);
        jwtConfig.setKey(new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName()));
    }

    public String generateAccessToken(UserDetails userDetails) {
        return createToken(new HashMap<>(), userDetails.getUsername(), jwtConfig.getAccessTokenExpirationTime());
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return createToken(new HashMap<>(), userDetails.getUsername(), jwtConfig.getRefreshTokenExpirationTime());
    }

    private String createToken(Map<String, Object> claims, String subject, long expirationTime) {
        return Jwts.builder()
                .setHeader(createHeader())
                .setClaims(createPayload(claims, subject, expirationTime))
                .signWith(jwtConfig.getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Map<String, Object> createHeader() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "HS256");
        headers.put("typ", "JWT");
        return headers;
    }

    private Map<String, Object> createPayload(Map<String, Object> claims, String subject, long expirationTime) {
        claims.put("sub", subject);
        claims.put("iat", new Date(System.currentTimeMillis()));
        claims.put("exp", new Date(System.currentTimeMillis() + expirationTime));
        return claims;
    }

    public Boolean isTokenValid(String token) {
        return !isTokenExpired(token) && getUsernameFromToken(token) != null;
    }

    public Boolean isTokenValidForUser(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimsFromToken(token, Claims::getExpiration);
    }

    public Date getIssuedAtDateFromToken(String token) {
        return getClaimsFromToken(token, Claims::getIssuedAt);
    }

    public <T> T getHeaderFromToken(String token, Function<JwsHeader<?>, T> headerResolver) {
        final JwsHeader<?> header = getAllHeadersFromToken(token);
        return headerResolver.apply(header);
    }

    private JwsHeader<?> getAllHeadersFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(jwtConfig.getKey()).build().parseClaimsJws(token).getHeader();
    }

    public <T> T getClaimsFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(jwtConfig.getKey()).build().parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }
}
