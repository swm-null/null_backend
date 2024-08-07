package com.example.oatnote.user.models;

import com.example.oatnote.user.service.models.User;

public record RegisterUserResponse(
    String id,
    String email,
    String password
) {

    public static RegisterUserResponse from(User user) {
        return new RegisterUserResponse(
            user.getId(),
            user.getEmail(),
            user.getPassword()
        );

    }
}
