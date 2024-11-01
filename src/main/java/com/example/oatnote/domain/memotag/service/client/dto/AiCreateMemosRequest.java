package com.example.oatnote.domain.memotag.service.client.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AiCreateMemosRequest(
    String content,
    String type,
    String userId
) {

    public static AiCreateMemosRequest from(String content, String type, String userId) {
        return new AiCreateMemosRequest(content, type, userId);
    }
}
