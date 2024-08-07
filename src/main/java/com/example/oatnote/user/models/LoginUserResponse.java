package com.example.oatnote.user.models;

public record LoginUserResponse(
    String accessToken,
    String refreshToken
) {

    public static LoginUserResponse of(String accessToken, String refreshToken) {
        return new LoginUserResponse(accessToken, refreshToken);
    }
}
