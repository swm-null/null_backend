package com.example.oatnote.memoTag.service.memo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.oatnote.memoTag.service.memo.model.Memo;

public interface MemoRepository extends MongoRepository<Memo, UUID> {

    @Query("{ 'content' : { $regex: ?0, $options: 'i' } }")
    List<Memo> findByContentRegex(String regex);

    Page<Memo> findAllByIdIn(List<UUID> id, Pageable pageable);
}
