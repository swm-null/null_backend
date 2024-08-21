package com.example.oatnote.user.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(SnakeCaseStrategy.class)
public record LoginUserResponse(
    @Schema(
        description = "액세스 토큰",
        example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLrr7zsp4BAZXhhbXBsZS5jb20iLCJpYXQiOjE3MjMwNjYyNjEsImV4cCI6MTcyMzA2Njg2MX0.BpieiOY2ueUgcmyToh0Br8KiNJMH-7hoOAKYwtxeP9s"
    )
    String accessToken,

    @Schema(
        description = "리프레시 토큰",
        example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLrr7zsp4BAZXhhbXBsZS5jb20iLCJpYXQiOjE3MjMwNjYyNjEsImV4cCI6MTcyMzE1MjY2MX0.IX1wMkmrw3a-U72zyOqRd4lwklE2tyL_jTpSpDbZFDo"
    )
    String refreshToken
) {

    public static LoginUserResponse of(String accessToken, String refreshToken) {
        return new LoginUserResponse(accessToken, refreshToken);
    }
}
