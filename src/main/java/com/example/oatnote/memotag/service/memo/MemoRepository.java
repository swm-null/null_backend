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

    Page<Memo> findByIdInAndUserId(List<String> memoIds, Pageable pageable, String userId);

    @Query("{ 'content' : { $regex: ?0, $options: 'i' }, 'userId' : ?1 }")
    List<Memo> findByContentRegexAndUserId(String regex, String userId);

    void deleteByUserId(String userId);
}
