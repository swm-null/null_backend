package com.example.oatnote.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record VerifyEmailRequest(
    @Email(message = "유효한 이메일 주소를 입력하세요.")
    String email,

    @NotBlank(message = "유효한 인증 코드를 입력하세요.")
    @Size(min = 6, max = 6, message = "인증 코드는 6자리 숫자입니다.")
    String code
) {

}
