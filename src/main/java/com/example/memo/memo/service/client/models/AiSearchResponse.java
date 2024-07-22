package com.example.memo.memo.service.client.models;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import java.util.List;

import com.example.memo.memo.service.enums.AiSearchType;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AiSearchResponse(
    AiSearchType type, // "similarity" | "regex" | "tag" | "unspecified"
    String processedMessage,
    List<String> ids,
    String regex,
    List<String> tags
) {

}
