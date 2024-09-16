package com.example.oatnote.user.dto;

import jakarta.validation.constraints.Email;

public record DispatchEmailRequest(
    @Email(message = "유효한 이메일 주소를 입력하세요.")
    String email
) {

}
