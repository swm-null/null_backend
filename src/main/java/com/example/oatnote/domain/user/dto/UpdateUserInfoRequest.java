package com.example.oatnote.domain.user.dto;

public record UpdateUserInfoRequest(
    String email,
    String name,
    String profileImageUrl
) {

}
