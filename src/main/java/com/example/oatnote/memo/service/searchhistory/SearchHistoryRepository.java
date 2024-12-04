package com.example.oatnote.memo.service.searchhistory;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.oatnote.memo.service.searchhistory.model.SearchHistory;

public interface SearchHistoryRepository extends MongoRepository<SearchHistory, String> {

    @Query(value = "{ '_id': ?0, 'userId': ?1 }", fields = "{ 'query': 1 }")
    Optional<SearchHistory> findQueryByIdAndUserId(String searchHistoryId, String userId);

    Page<SearchHistory> findByUserId(Pageable pageable, String userId);

    void deleteByUserId(String userId);

    Integer countByUserId(String userId);
}
