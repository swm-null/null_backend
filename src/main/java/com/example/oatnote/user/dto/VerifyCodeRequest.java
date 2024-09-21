package com.example.oatnote.user.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record VerifyCodeRequest(
    @Schema(description = "이메일 주소", example = "user@example.com", requiredMode = REQUIRED)
    @Email(message = "유효한 이메일 주소를 입력하세요.")
    String email,

    @Schema(description = "인증 코드", example = "123456", requiredMode = REQUIRED)
    @NotBlank(message = "유효한 인증 코드를 입력하세요.")
    @Size(min = 6, max = 6, message = "인증 코드는 6자리 숫자입니다.")
    String code
) {

}
