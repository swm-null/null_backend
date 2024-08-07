package com.example.oatnote.user.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@JsonNaming(SnakeCaseStrategy.class)
public record RegisterUserRequest(
    @Schema(description = "이메일 주소", example = "user@example.com", requiredMode = RequiredMode.REQUIRED)
    @NotBlank(message = "이메일은 비어 있을 수 없습니다.")
    @Email(message = "유효한 이메일 주소를 입력하세요.")
    String email,

    @Schema(description = "비밀번호", example = "password123", requiredMode = RequiredMode.REQUIRED)
    @NotBlank(message = "비밀번호는 비어 있을 수 없습니다.")
    @Size(min = 5, message = "비밀번호는 최소 5자 이상이어야 합니다.")
    String password
) {

}
