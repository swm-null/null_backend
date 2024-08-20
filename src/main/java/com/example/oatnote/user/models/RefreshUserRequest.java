package com.example.oatnote.user.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;

@JsonNaming(SnakeCaseStrategy.class)
public record RefreshUserRequest(
    @Schema(
        description = "리프레시 토큰",
        example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiLrr7zsp4BAZXhhbXBsZS5jb20iLCJpYXQiOjE3MjMwNjYyNjEsImV4cCI6MTcyMzE1MjY2MX0.IX1wMkmrw3a-U72zyOqRd4lwklE2tyL_jTpSpDbZFDo",
        requiredMode = RequiredMode.REQUIRED
    )
    @NotBlank(message = "리프레시 토큰은 비어 있을 수 없습니다.")
    String refreshToken
) {

}
