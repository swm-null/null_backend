package com.example.oatnote.memotag.service.memo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.oatnote.memotag.service.memo.model.Memo;

public interface MemoRepository extends MongoRepository<Memo, String> {

    Optional<Memo> findByIdAndUserId(String memoId, String userId);

    List<Memo> findByIdInAndUserId(List<String> memoIds, String userId);

    Page<Memo> findByIdInAndUserId(List<String> memoIds, String userId, Pageable pageable);

    void deleteByIdAndUserId(String memoId, String userId);

    void deleteByIdInAndUserId(List<String> memoIds, String userId);

    void deleteByUserId(String userId);

    @Query(value = "{ '_id': { '$in': ?0 }, 'userId': ?1 }", fields = "{ 'imageUrls': 1, 'voiceUrls': 1 }")
    List<Memo> findFileUrlsByIdInAndUserId(List<String> memoIds, String userId);
}
