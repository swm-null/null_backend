package com.example.oatnote.domain.user.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record UpdateUserInfoRequest(
    String email,
    String name,
    String profileImageUrl
) {

}
