package com.example.oatnote.memo.service.client.models;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import java.util.List;

import com.example.oatnote.memo.service.client.models.enums.AiSearchType;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotNull;

@JsonNaming(SnakeCaseStrategy.class)
public record AiSearchMemoResponse(
    @NotNull(message = "검색 타입은 비워둘 수 없습니다.")
    AiSearchType type, // "similarity" | "regex" | "tag"
    String processedMessage,
    List<String> ids,
    String regex,
    List<String> tags
) {

}
