package com.example.oatnote.memoTag.service.client.dto;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AISearchMemoRequest(
    String content,
    String userId
) {

    public static AISearchMemoRequest from(String content, String userId) {
        return new AISearchMemoRequest(content, userId);
    }
}
