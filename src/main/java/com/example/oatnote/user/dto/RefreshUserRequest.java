package com.example.oatnote.user.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@JsonNaming(SnakeCaseStrategy.class)
public record RefreshUserRequest(
    @Schema(description = "리프레시 토큰", example = "eyJhbGciOiJI...", requiredMode = REQUIRED)
    @NotBlank(message = "리프레시 토큰은 비어 있을 수 없습니다.")
    String refreshToken
) {

}
