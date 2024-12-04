package com.example.oatnote.user.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(SnakeCaseStrategy.class)
public record RefreshUserResponse(
    @Schema(description = "액세스 토큰", example = "eyJhbGciOiJIUzI...")
    String accessToken,

    @Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI...")
    String refreshToken
) {

    public static RefreshUserResponse of(String newAccessToken, String refreshToken) {
        return new RefreshUserResponse(newAccessToken, refreshToken);
    }
}
