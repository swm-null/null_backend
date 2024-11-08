package com.example.oatnote.domain.memotag.dto.innerDto;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import java.time.LocalDateTime;

import com.example.oatnote.domain.memotag.dto.SearchMemosUsingAiResponse;
import com.example.oatnote.domain.memotag.dto.SearchMemosUsingDbResponse;
import com.example.oatnote.domain.memotag.service.searchhistory.model.SearchHistory;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record SearchHistoryResponse(
    String id,
    String query,
    SearchMemosUsingAiResponse ai,
    SearchMemosUsingDbResponse db,
    LocalDateTime createdAt
) {

    public static SearchHistoryResponse from(SearchHistory searchHistory) {
        return new SearchHistoryResponse(
            searchHistory.getId(),
            searchHistory.getQuery(),
            searchHistory.getSearchMemosUsingAiResponse(),
            searchHistory.getSearchMemosUsingDbResponse(),
            searchHistory.getCreatedAt()
        );
    }
}
