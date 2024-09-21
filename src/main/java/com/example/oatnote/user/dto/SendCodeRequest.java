package com.example.oatnote.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;

public record SendCodeRequest(
    @Schema(description = "이메일 주소", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @Email(message = "유효한 이메일 주소를 입력하세요.")
    String email
) {

}