package com.example.oatnote.domain.user.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@JsonNaming(SnakeCaseStrategy.class)
public record LoginUserRequest(
    @Schema(description = "이메일 주소", example = "user@example.com", requiredMode = REQUIRED)
    @Email(message = "유효한 이메일 주소를 입력하세요.")
    @NotBlank(message = "이메일은 비어있을 수 없습니다.")
    String email,

    @Schema(description = "비밀번호", example = "password123!", requiredMode = REQUIRED)
    @NotBlank(message = "비밀번호를 입력해주세요.")
    String password
) {

}
