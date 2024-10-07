package com.example.oatnote.domain.user.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(SnakeCaseStrategy.class)
public record LoginUserResponse(
    @Schema(description = "액세스 토큰", example = "eyJhbGciOiJI...")
    String accessToken,

    @Schema(description = "리프레시 토큰", example = "eyJhbGciOiJI...")
    String refreshToken
) {

    public static LoginUserResponse of(String accessToken, String refreshToken) {
        return new LoginUserResponse(accessToken, refreshToken);
    }
}
