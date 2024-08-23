package com.example.spring_security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

    private SecretKey key;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpirationTime;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpirationTime;

    @Value("${jwt.secret}")
    private String secret;

    @PostConstruct
    public void init() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        //keyBytes = Base64.getDecoder().decode(keyBytes);
        this.key = new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername(), accessTokenExpirationTime);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername(), refreshTokenExpirationTime);
    }


    private String createToken(Map<String, Object> claims, String subject, long expirationTime) {
        Map<String, Object> headers = createHeader();
        Map<String, Object> payload = createPayload(claims, subject, expirationTime);
        String token = signToken(headers, payload);

        System.out.println(getAllHeadersFromToken(token));
        System.out.println(getAllClaimsFromToken(token));
        System.out.println(isTokenValid(token));

        return token;
    }

    private Map<String, Object> createHeader() {
        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "HS256"); // Algoritmul de semnătură
        headers.put("typ", "JWT");   // Tipul de token
        return headers;
    }

    private Map<String, Object> createPayload(Map<String, Object> claims, String subject, long expirationTime) {
        claims.put("sub", subject); // Setează subiectul tokenului
        claims.put("iat", new Date(System.currentTimeMillis())); // Data emiterii
        claims.put("exp", new Date(System.currentTimeMillis() + expirationTime)); // Data expirării
        return claims;
    }

    private String signToken(Map<String, Object> headers, Map<String, Object> payload) {
        return Jwts.builder()
                .setHeader(headers)
                .setClaims(payload)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public Boolean isTokenValidForUser(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


    public <T> T getHeaderFromToken(String token, Function<JwsHeader<?>, T> headerResolver) {
        final JwsHeader<?> header = getAllHeadersFromToken(token);
        return headerResolver.apply(header);
    }

    private JwsHeader<?> getAllHeadersFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getHeader();
    }

    public String getAlgorithmFromToken(String token) {
        return getHeaderFromToken(token, JwsHeader::getAlgorithm);
    }

    public String getTypeFromToken(String token) {
        return getHeaderFromToken(token, header -> (String) header.get("typ"));
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

    public <T> T getClaimsFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public boolean isTokenValid(String token) {
        return !isTokenExpired(token) && getUsernameFromToken(token) != null;
    }
}
