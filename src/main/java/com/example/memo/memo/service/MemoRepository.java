package com.example.memo.memo.service;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.memo.memo.service.models.Memo;

public interface MemoRepository extends MongoRepository<Memo, String> {

    @Query("{ 'content' : { $regex: ?0, $options: 'i' } }")
    List<Memo> findAllByContentRegex(String regex);

    List<Memo> findAllByTagIdsIn(List<String> tagIds);
}
