package com.example.oatnote.domain.memotag.service.client.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotBlank;

@JsonNaming(SnakeCaseStrategy.class)
public record AICreateEmbeddingRequest(
    String content
) {

    public static AICreateEmbeddingRequest from(String content) {
        return new AICreateEmbeddingRequest(content);
    }
}
