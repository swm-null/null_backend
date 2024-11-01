package com.example.oatnote.domain.memotag.service.client.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AiCreateEmbeddingRequest(
    String content
) {

    public static AiCreateEmbeddingRequest from(String content) {
        return new AiCreateEmbeddingRequest(content);
    }
}
