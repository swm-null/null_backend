package com.example.oatnote.user.dto;

public record UpdateUserInfoRequest(
    String email,
    String name,
    String profileImageUrl
) {

}
