package com.example.oatnote.domain.memotag.service.aiClient.dto;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AiSearchMemosUsingAiRequest(
    String query,
    String userId
) {

    public static AiSearchMemosUsingAiRequest of(String query, String userId) {
        return new AiSearchMemosUsingAiRequest(
            query,
            userId
        );
    }
}