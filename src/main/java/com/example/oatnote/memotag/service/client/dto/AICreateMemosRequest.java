package com.example.oatnote.memotag.service.client.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AICreateMemosRequest(
    String content,
    String type,
    String userId
) {

    public static AICreateMemosRequest from(String content, String type, String userId) {
        return new AICreateMemosRequest(content, type, userId);
    }
}
