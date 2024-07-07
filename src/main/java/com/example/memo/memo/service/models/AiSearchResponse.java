package com.example.memo.memo.service.models;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AiSearchResponse(
    String type, // "similarity" | "tag" | "regex" | "unspecified"
    String processedMessage,
    List<String> ids,
    String regex,
    List<String> tags
) {

}
