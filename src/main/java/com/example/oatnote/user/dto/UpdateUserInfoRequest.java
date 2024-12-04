package com.example.oatnote.user.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@JsonNaming(SnakeCaseStrategy.class)
public record UpdateUserInfoRequest(
    @Schema(description = "이메일 주소", example = "user@example.com", requiredMode = REQUIRED)
    @Email(message = "유효한 이메일 주소를 입력하세요.")
    @NotBlank(message = "이메일은 비어 있을 수 없습니다.")
    String email,

    @Schema(description = "이름", example = "bbo", requiredMode = REQUIRED)
    @Size(max = 10, message = "이름은 최대 10자입니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9]+$", message = "한글, 영문 및 숫자만 사용할 수 있습니다.")
    @NotBlank(message = "이름은 비어 있을 수 없습니다.")
    String name,

    @Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.jpg")
    @NotBlank(message = "프로필 이미지 URL은 비어 있을 수 없습니다.")
    String profileImageUrl
) {

}
