package com.example.oatnote.domain.memotag.service.client.dto;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AISearchMemosRequest(
    String query,
    String userId
) {

    public static AISearchMemosRequest of(String query, String userId) {
        return new AISearchMemosRequest(
            query,
            userId
        );
    }
}
