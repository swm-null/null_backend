package com.example.oatnote.domain.memotag.service.client.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AiSearchMemosUsingDbRequest(
    String query,
    String userId
) {

    public static AiSearchMemosUsingDbRequest of(String query, String userId) {
        return new AiSearchMemosUsingDbRequest(
            query,
            userId
        );
    }
}
