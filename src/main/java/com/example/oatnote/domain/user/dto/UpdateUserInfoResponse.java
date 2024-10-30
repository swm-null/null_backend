package com.example.oatnote.domain.user.dto;

import com.example.oatnote.domain.user.service.model.User;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record UpdateUserInfoResponse(
    String email,
    String name,
    String profileImageUrl
) {

    public static UpdateUserInfoResponse from(User updatedUser) {
        return new UpdateUserInfoResponse(
            updatedUser.getEmail(),
            updatedUser.getName(),
            updatedUser.getProfileImageUrl()
        );
    }
}
