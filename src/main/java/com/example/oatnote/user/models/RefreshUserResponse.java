package com.example.oatnote.user.models;

public record RefreshUserResponse(
    String accessToken,
    String refreshToken
) {

    public static RefreshUserResponse of(String newAccessToken, String refreshToken) {
        return new RefreshUserResponse(newAccessToken, refreshToken);
    }
}
