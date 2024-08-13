package com.example.oatnote.util;

import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private final byte[] secretKey;
    private final Long accessTokenExpirationTime;
    private final Long refreshTokenExpirationTime;

    public JwtUtil(
        @Value("${jwt.secret-key}") String secretKey,
        @Value("${jwt.access-token.expiration-time}") Long accessTokenExpirationTime,
        @Value("${jwt.refresh-token.expiration-time}") Long refreshTokenExpirationTime
    ) {
        this.secretKey = secretKey.getBytes();
        this.accessTokenExpirationTime = accessTokenExpirationTime;
        this.refreshTokenExpirationTime = refreshTokenExpirationTime;
    }

    public String generateAccessToken(String email) {
        return createToken(email, accessTokenExpirationTime);
    }

    public String generateRefreshToken(String email) {
        return createToken(email, refreshTokenExpirationTime);
    }

    private String createToken(String subject, long expirationTime) {
        return Jwts.builder()
            .setSubject(subject)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
            .signWith(Keys.hmacShaKeyFor(secretKey), SignatureAlgorithm.HS256)
            .compact();
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public void validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtException("Invalid JWT token", e);
        }
    }
}
