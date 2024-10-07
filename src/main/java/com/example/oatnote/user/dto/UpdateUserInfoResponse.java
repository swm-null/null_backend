package com.example.oatnote.user.dto;

import com.example.oatnote.user.service.model.User;

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
