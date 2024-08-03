package com.example.oatnote.memo.service.memo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.oatnote.memo.service.memo.models.Memo;

public interface MemoRepository extends MongoRepository<Memo, String> {

    @Query("{ 'content' : { $regex: ?0, $options: 'i' } }")
    List<Memo> findByContentRegex(String regex);

    List<Memo> findByTagIdsIn(List<String> tagIds);
}
