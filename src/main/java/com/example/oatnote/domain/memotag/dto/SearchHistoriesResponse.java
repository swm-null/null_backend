package com.example.oatnote.domain.memotag.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.oatnote.domain.memotag.dto.innerDto.SearchHistoryResponse;
import com.example.oatnote.web.model.Criteria;

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
