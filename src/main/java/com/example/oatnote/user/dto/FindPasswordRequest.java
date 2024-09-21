package com.example.oatnote.user.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record FindPasswordRequest(
    @Schema(description = "이메일 주소", example = "user@example.com", requiredMode = REQUIRED)
    @Email(message = "유효한 이메일 주소를 입력하세요.")
    String email,

    @Schema(description = "새 비밀번호", example = "password123!", requiredMode = REQUIRED)
    @NotBlank(message = "비밀번호는 비어 있을 수 없습니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    @Pattern(
        regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
        message = "비밀번호는 8자 이상의 영어, 숫자, 특수기호의 조합이어야 합니다."
    )
    String newPassword,

    @Schema(description = "비밀번호 확인", example = "password123!", requiredMode = REQUIRED)
    @NotBlank(message = "비밀번호 확인은 비어 있을 수 없습니다.")
    String confirmPassword
) {

}
