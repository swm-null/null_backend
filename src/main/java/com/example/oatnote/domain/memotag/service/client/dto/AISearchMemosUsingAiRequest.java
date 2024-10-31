package com.example.oatnote.domain.memotag.service.client.dto;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AISearchMemosUsingAiRequest(
    String query,
    String userId
) {

    public static AISearchMemosUsingAiRequest of(String query, String userId) {
        return new AISearchMemosUsingAiRequest(
            query,
            userId
        );
    }
}
