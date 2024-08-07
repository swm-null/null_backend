package com.example.oatnote.user.models;

public record LoginUserRequest(
    String email,
    String password
) {

}
