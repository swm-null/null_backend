package com.example.oatnote.memoTag.service.client.dto;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import java.util.List;
import java.util.UUID;

import com.example.oatnote.memoTag.service.client.dto.enums.AISearchType;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotNull;

@JsonNaming(SnakeCaseStrategy.class)
public record AISearchMemoResponse(
    @NotNull(message = "검색 타입은 비워둘 수 없습니다.")
    AISearchType type, // "similarity" | "regex" | "tag"
    String processedMessage,
    List<UUID> ids,
    String regex,
    List<UUID> tags
) {

}
