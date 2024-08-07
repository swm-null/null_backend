package com.example.oatnote.user.models;

public record LoginUserResponse(
    String accessToken,
    String refreshToken
) {

}
