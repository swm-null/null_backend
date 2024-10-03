package com.example.oatnote.memotag.service.searchhistory;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.oatnote.memotag.dto.SearchMemosResponse;
import com.example.oatnote.memotag.service.searchhistory.model.SearchHistory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchHistoryService {

    private final SearchHistoryRepository searchHistoryRepository;

    public void createSearchHistory(SearchHistory searchHistory) {
        searchHistoryRepository.insert(searchHistory);
    }

    public Page<SearchHistory> getSearchHistories(String searchTerm, PageRequest pageRequest, String userId) {
        return searchHistoryRepository.findBySearchTermContainingAndUserId(searchTerm, pageRequest, userId);
    }

    public Integer countSearchHistories(String userId) {
        return searchHistoryRepository.countByUserId(userId);
    }
}
