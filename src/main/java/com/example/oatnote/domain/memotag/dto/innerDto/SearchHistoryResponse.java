package com.example.oatnote.domain.memotag.dto.innerDto;

import java.time.LocalDateTime;

import com.example.oatnote.domain.memotag.dto.SearchMemosResponse;
import com.example.oatnote.domain.memotag.service.searchhistory.model.SearchHistory;

public record SearchHistoryResponse(
    String query,
    LocalDateTime createdAt,
    SearchMemosResponse searchMemosResponse
) {

    public static SearchHistoryResponse from(SearchHistory searchHistory) {
        return new SearchHistoryResponse(
            searchHistory.getQuery(),
            searchHistory.getSearchedAt(),
            searchHistory.getSearchMemosResponse()
        );
    }
}
