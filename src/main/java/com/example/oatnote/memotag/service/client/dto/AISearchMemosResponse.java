package com.example.oatnote.memotag.service.client.dto;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import java.util.List;

import com.example.oatnote.memotag.service.client.dto.enums.AISearchTypeEnum;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AISearchMemosResponse(
    AISearchTypeEnum type, // "similarity" | "regex"
    String processedMessage,
    List<String> ids,
    String regex
) {

}
