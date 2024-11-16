package com.example.oatnote._commons.util;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.oatnote.web.controller.exception.auth.OatAuthorizationException;
import com.example.oatnote.web.controller.exception.auth.OatExpiredAccessTokenException;
import com.example.oatnote.web.controller.exception.auth.OatExpiredRefreshTokenException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private final Key secretKey;
    private final Long accessTokenExpirationTime;
    private final Long refreshTokenExpirationTime;

    private static final String ACCESS_TOKEN_TYPE = "ACCESS";
    private static final String REFRESH_TOKEN_TYPE = "REFRESH";

    public JwtUtil(
        @Value("${jwt.secret-key}") String secretKey,
        @Value("${jwt.access-token.expiration-ms}") Long accessTokenExpirationTime,
        @Value("${jwt.refresh-token.expiration-ms}") Long refreshTokenExpirationTime
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpirationTime = accessTokenExpirationTime;
        this.refreshTokenExpirationTime = refreshTokenExpirationTime;
    }

    public String generateAccessToken(String userId) {
        return createToken(userId, accessTokenExpirationTime, ACCESS_TOKEN_TYPE);
    }

    public String generateRefreshToken(String userId) {
        return createToken(userId, refreshTokenExpirationTime, REFRESH_TOKEN_TYPE);
    }

    private String createToken(String subject, long expirationTime, String tokenType) {
        return Jwts.builder()
            .setSubject(subject)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
            .claim("token_type", tokenType)
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact();
    }

    public String extractUserId(String token) {
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

    public void validateAccessToken(String token) throws OatAuthorizationException {
        validateTokenType(token, ACCESS_TOKEN_TYPE);
    }

    public void validateRefreshToken(String token) throws OatAuthorizationException {
        validateTokenType(token, REFRESH_TOKEN_TYPE);
    }

    private void validateTokenType(String token, String expectedTokenType) throws OatAuthorizationException {
        Claims claims;
        try {
            claims = extractAllClaims(token);
        } catch (ExpiredJwtException e) {
            throw getExpiredTokenException(expectedTokenType);
        } catch (Exception e) {
            throw OatAuthorizationException.withDetail("유효하지 않은 토큰입니다.");
        }

        String tokenType = claims.get("token_type", String.class);
        if (!tokenType.equals(expectedTokenType)) {
            throw OatAuthorizationException.withDetail("토큰 타입이 올바르지 않습니다.");
        }
    }

    private OatAuthorizationException getExpiredTokenException(String expectedTokenType) {
        return switch (expectedTokenType) {
            case ACCESS_TOKEN_TYPE -> OatExpiredAccessTokenException.withDetail("액세스 토큰이 만료되었습니다.");
            case REFRESH_TOKEN_TYPE -> OatExpiredRefreshTokenException.withDetail("리프레시 토큰이 만료되었습니다.");
            default -> throw OatAuthorizationException.withDetail("알 수 없는 토큰 타입입니다.");
        };
    }
}
