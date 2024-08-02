package com.example.oatnote.memo.service.client.models;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import java.util.List;

import com.example.oatnote.memo.service.client.models.enums.AiSearchType;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AiSearchMemoResponse(
    AiSearchType type, // "similarity" | "regex" | "tag"
    String processedMessage,
    List<String> ids,
    String regex,
    List<String> tags
) {

}
