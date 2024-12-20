package com.example.oatnote.memotag.dto.innerDto;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import java.time.LocalDateTime;

import com.example.oatnote.memotag.dto.SearchMemosUsingAiResponse;
import com.example.oatnote.memotag.dto.SearchMemosUsingDbResponse;
import com.example.oatnote.memotag.service.searchhistory.model.SearchHistory;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record SearchHistoryResponse(
    String id,
    String query,
    SearchMemosUsingAiResponse ai,
    SearchMemosUsingDbResponse db,
    LocalDateTime createdAt
) {

    public static SearchHistoryResponse from(
        SearchHistory searchHistory,
        SearchMemosUsingAiResponse searchMemosUsingAiResponse,
        SearchMemosUsingDbResponse searchMemosUsingDbResponse
    ) {
        return new SearchHistoryResponse(
            searchHistory.getId(),
            searchHistory.getQuery(),
            searchMemosUsingAiResponse,
            searchMemosUsingDbResponse,
            searchHistory.getCreatedAt()
        );
    }
}
