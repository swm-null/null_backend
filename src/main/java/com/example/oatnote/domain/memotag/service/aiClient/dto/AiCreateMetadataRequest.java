package com.example.oatnote.domain.memotag.service.aiClient.dto;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AiCreateMetadataRequest(
    String content,
    List<String> imageUrls,
    List<String> voiceUrls
) {

    public static AiCreateMetadataRequest from(String content, List<String> imageUrls, List<String> voiceUrls) {
        return new AiCreateMetadataRequest(content, imageUrls, voiceUrls);
    }
}
