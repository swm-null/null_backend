package com.example.oatnote.memotag.dto.innerDto;

import java.time.LocalDateTime;

import com.example.oatnote.memotag.dto.SearchMemosResponse;
import com.example.oatnote.memotag.service.searchhistory.model.SearchHistory;

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
