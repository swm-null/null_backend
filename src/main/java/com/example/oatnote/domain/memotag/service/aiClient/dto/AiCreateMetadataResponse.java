package com.example.oatnote.domain.memotag.service.aiClient.dto;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AiCreateMetadataResponse(
    String metadata,
    List<Double> embeddingMetadata
) {

}
