package com.example.oatnote.domain.memotag.service.searchhistory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.oatnote.domain.memotag.service.searchhistory.model.SearchHistory;

public interface SearchHistoryRepository extends MongoRepository<SearchHistory, String> {

    @Query("{ 'query': { $regex: ?0, $options: 'i' }, 'userId': ?1 }")
    Page<SearchHistory> findBySearchTermContainingAndUserId(String query, Pageable pageable, String userId);

    Integer countByUserId(String userId);

    void deleteByUserId(String userId);
}
