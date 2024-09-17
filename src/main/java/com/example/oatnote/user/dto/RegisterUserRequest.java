package com.example.oatnote.user.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@JsonNaming(SnakeCaseStrategy.class)
public record RegisterUserRequest(
    @Schema(description = "이메일 주소", example = "user@example.com", requiredMode = RequiredMode.REQUIRED)
    @Email(message = "유효한 이메일 주소를 입력하세요.")
    String email,

    @Schema(description = "비밀번호", example = "password123!", requiredMode = RequiredMode.REQUIRED)
    @NotBlank(message = "비밀번호는 비어 있을 수 없습니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    @Pattern(
        regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
        message = "비밀번호는 8자 이상의 영어, 숫자, 특수기호의 조합이어야 합니다."
    )
    String password,

    @Schema(description = "비밀번호 확인", example = "password123!", requiredMode = RequiredMode.REQUIRED)
    @NotBlank(message = "비밀번호 확인은 비어 있을 수 없습니다.")
    String confirmPassword,

    @Schema(description = "이름", example = "bbo", requiredMode = RequiredMode.REQUIRED)
    @Size(max = 10, message = "이름은 최대 10자입니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9]+$", message = "한글, 영문 및 숫자만 사용할 수 있습니다.")
    String name,

    @Schema(description = "이메일 인증 여부", example = "true", requiredMode = RequiredMode.REQUIRED)
    boolean isEmailVerified
) {

}
