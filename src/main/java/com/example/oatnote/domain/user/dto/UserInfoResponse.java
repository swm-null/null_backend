package com.example.oatnote.domain.user.dto;

import com.example.oatnote.domain.user.service.model.User;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserInfoResponse(
    String email,
    String name,
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
