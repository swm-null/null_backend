package com.example.oatnote.memotag.service.searchhistory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.oatnote.memotag.service.searchhistory.model.SearchHistory;

public interface SearchHistoryRepository extends MongoRepository<SearchHistory, String> {

    @Query("{ 'searchTerm': { $regex: ?0, $options: 'i' }, 'userId': ?1 }")
    Page<SearchHistory> findBySearchTermContainingAndUserId(String searchTerm, Pageable pageable, String userId);

    Integer countByUserId(String userId);
}
