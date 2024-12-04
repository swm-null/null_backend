package com.example.oatnote.memo.dto;

import com.example.oatnote.memo.service.searchhistory.model.SearchHistory;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@JsonNaming(SnakeCaseStrategy.class)
public record CreateSearchHistoryRequest(
    @Schema(description = "검색어", example = "우리 멘토링 일정 언제더라?")
    @NotBlank(message = "검색어는 비어있을 수 없습니다.")
    String query
) {

    public SearchHistory toSearchHistory(String userId) {
        return new SearchHistory(query, userId);
    }
}
