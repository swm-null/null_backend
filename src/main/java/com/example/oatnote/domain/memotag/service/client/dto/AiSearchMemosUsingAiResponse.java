package com.example.oatnote.domain.memotag.service.client.dto;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import java.util.List;

import com.example.oatnote.domain.memotag.service.client.dto.enums.AiSearchTypeEnum;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AiSearchMemosUsingAiResponse(
    AiSearchTypeEnum type, // "similarity" | "regex"
    String processedMessage,
    List<String> memoIds,
    String regex
) {

}
