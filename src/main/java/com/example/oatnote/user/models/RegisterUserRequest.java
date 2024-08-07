package com.example.oatnote.user.models;

public record RegisterUserRequest(
    String id,
    String email,
    String password
) {
}
