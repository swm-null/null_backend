package com.example.oatnote.domain.memotag.dto.innerDto;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import java.time.LocalDateTime;

import com.example.oatnote.domain.memotag.dto.SearchMemosResponse;
import com.example.oatnote.domain.memotag.service.searchhistory.model.SearchHistory;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record SearchHistoryResponse(
    String query,
    LocalDateTime createdAt,
    SearchMemosResponse searchMemosResponse
) {

    public static SearchHistoryResponse from(SearchHistory searchHistory) {
        return new SearchHistoryResponse(
            searchHistory.getQuery(),
            searchHistory.getCreatedAt(),
            searchHistory.getSearchMemosResponse()
        );
    }
}
