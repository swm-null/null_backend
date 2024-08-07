package com.example.oatnote.user.models;

import jakarta.validation.constraints.NotBlank;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@JsonNaming(SnakeCaseStrategy.class)
public record RefreshUserRequest(
    @Schema(description = "리프레시 토큰", example = "d2VyeSEiOiJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", requiredMode = RequiredMode.REQUIRED)
    @NotBlank(message = "리프레시 토큰은 비어 있을 수 없습니다.")
    String refreshToken
) {

}
