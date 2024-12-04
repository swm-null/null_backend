package com.example.oatnote.user.dto;

import com.example.oatnote.user.service.model.User;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserInfoResponse(
    @Schema(description = "이메일 주소", example = "user@example.com")
    String email,

    @Schema(description = "유저 이름", example = "유저123")
    String name,

    @Schema(description = "프로필 이미지 URL", example = "https://example.com/profile.jpg")
    String profileImageUrl
) {

    public static UserInfoResponse from(User user) {
        return new UserInfoResponse(
            user.getEmail(),
            user.getName(),
            user.getProfileImageUrl()
        );
    }

}
