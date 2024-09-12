package com.example.oatnote.memotag.service.client.dto;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import java.util.List;

import com.example.oatnote.memotag.service.client.dto.enums.AISearchType;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AISearchMemoResponse(
    AISearchType type, // "similarity" | "regex" | "tag"
    String processedMessage,
    List<String> ids,
    String regex,
    List<String> tags
) {

}
