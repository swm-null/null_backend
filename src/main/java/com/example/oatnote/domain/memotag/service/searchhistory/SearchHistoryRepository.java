package com.example.oatnote.domain.memotag.service.searchhistory;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.oatnote.domain.memotag.service.searchhistory.model.SearchHistory;

public interface SearchHistoryRepository extends MongoRepository<SearchHistory, String> {

    @Query(value = "{ '_id': ?0, 'userId': ?1 }", fields = "{ 'query': 1 }")
    Optional<SearchHistory> findQueryByIdAndUserId(String searchHistoryId, String userId);

    @Query("{ 'query': { $regex: ?0, $options: 'i' }, 'userId': ?1 }")
    Page<SearchHistory> findBySearchTermContainingAndUserId(String query, Pageable pageable, String userId);

    void deleteByUserId(String userId);

    Integer countByUserId(String userId);
}
