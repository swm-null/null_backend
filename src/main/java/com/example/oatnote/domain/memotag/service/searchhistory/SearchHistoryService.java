package com.example.oatnote.domain.memotag.service.searchhistory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.oatnote.domain.memotag.service.searchhistory.model.SearchHistory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchHistoryService {

    private final SearchHistoryRepository searchHistoryRepository;

    public void createSearchHistory(SearchHistory searchHistory) {
        log.info("검색 히스토리 생성 - 검색어: {} / 유저: {}", searchHistory.getQuery(), searchHistory.getUserId());
        searchHistoryRepository.insert(searchHistory);
    }

    public Page<SearchHistory> getSearchHistories(String query, PageRequest pageRequest, String userId) {
        return searchHistoryRepository.findBySearchTermContainingAndUserId(query, pageRequest, userId);
    }

    public Integer countSearchHistories(String userId) {
        return searchHistoryRepository.countByUserId(userId);
    }
}
