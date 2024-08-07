package com.example.oatnote.user.models;

public record RefreshUserResponse(
    String accessToken,
    String refreshToken
) {

}
