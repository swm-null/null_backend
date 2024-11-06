package com.example.oatnote.domain.memotag.service.searchhistory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.example.oatnote.domain.memotag.dto.SearchMemosUsingAiResponse;
import com.example.oatnote.domain.memotag.dto.SearchMemosUsingDbResponse;
import com.example.oatnote.domain.memotag.service.searchhistory.model.SearchHistory;
import com.example.oatnote.web.controller.exception.client.OatDataNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchHistoryService {

    private final SearchHistoryRepository searchHistoryRepository;
    private final MongoTemplate mongoTemplate;

    public SearchHistory createSearchHistory(SearchHistory searchHistory) {
        log.info("검색 히스토리 생성 / 검색어: {} / 유저: {}", searchHistory.getQuery(), searchHistory.getUserId());
        return searchHistoryRepository.insert(searchHistory);
    }

    public String getQuery(String searchHistoryId, String userId) {
        return searchHistoryRepository.findQueryByIdAndUserId(searchHistoryId, userId)
            .map(SearchHistory::getQuery)
            .orElseThrow(() -> OatDataNotFoundException.withDetail("검색 기록를 찾을 수 없습니다.", searchHistoryId));
    }

    public Page<SearchHistory> getSearchHistories(String query, PageRequest pageRequest, String userId) {
        return searchHistoryRepository.findBySearchTermContainingAndUserId(query, pageRequest, userId);
    }

    public void updateAiResponse(String searchHistoryId, SearchMemosUsingAiResponse aiResponse, String userId) {
        log.info("AI 검색 결과 업데이트 / 검색 히스토리: {} / 유저: {}", searchHistoryId, userId);
        Query query = new Query(Criteria.where("_id").is(searchHistoryId).and("uId").is(userId));
        Update update = new Update().set("aiRes", aiResponse);
        mongoTemplate.updateFirst(query, update, SearchHistory.class);
    }

    public void updateDbResponse(String searchHistoryId, SearchMemosUsingDbResponse dbResponse, String userId) {
        log.info("DB 검색 결과 업데이트 / 검색 히스토리: {} / 유저: {}", searchHistoryId, userId);
        Query query = new Query(Criteria.where("_id").is(searchHistoryId).and("uId").is(userId));
        Update update = new Update().set("dbRes", dbResponse);
        mongoTemplate.updateFirst(query, update, SearchHistory.class);
    }

    public void deleteUserAllData(String userId) {
        log.info("검색 히스토리 전체 삭제 / 유저: {}", userId);
        searchHistoryRepository.deleteByUserId(userId);
    }

    public Integer countSearchHistories(String userId) {
        return searchHistoryRepository.countByUserId(userId);
    }
}
