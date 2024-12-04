package com.example.oatnote.memo.dto;

import com.example.oatnote.memo.service.searchhistory.model.SearchHistory;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateSearchHistoryResponse(
    @Schema(description = "검색어 고유 ID", example = "61b72b3e9b1e8b1e4c8b4560")
    String id,

    @Schema(description = "검색어", example = "우리 멘토링 일정 언제더라?")
    String query
) {

    public static CreateSearchHistoryResponse from(SearchHistory createdSearchHistory) {
        return new CreateSearchHistoryResponse(
            createdSearchHistory.getId(),
            createdSearchHistory.getQuery()
        );
    }
}
