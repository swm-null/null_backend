package com.example.oatnote.memo.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.oatnote.memo.dto.innerDto.SearchHistoryResponse;
import com.example.oatnote.web.model.Criteria;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record SearchHistoriesResponse(
    Long totalCount,
    Integer currentCount,
    Integer totalPage,
    Integer currentPage,
    List<SearchHistoryResponse> searchHistories
) {

    public static SearchHistoriesResponse from(
        Page<SearchHistoryResponse> pagedResult,
        Criteria criteria
    ) {
        return new SearchHistoriesResponse(
            pagedResult.getTotalElements(),
            pagedResult.getContent().size(),
            pagedResult.getTotalPages(),
            criteria.getCurrentPage(),
            pagedResult.getContent()
        );
    }
}
