package com.example.memo.memo.service;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.memo.memo.service.exception.MemoNotFoundException;
import com.example.memo.memo.service.models.Memo;

public interface MemoRepository extends MongoRepository<Memo, String> {

    @Query("{ 'content' : { $regex: ?0, $options: 'i' } }")
    List<Memo> findByContentRegex(String regex);

    List<Memo> findByTagIdsContaining(String tagId);

    default Memo getById(String id) {
        return findById(id)
            .orElseThrow(() -> new MemoNotFoundException("존재하지 않는 메모 id 입니다."));
    }

    default List<Memo> getByContentRegex(String regex) {
        List<Memo> memos = findByContentRegex(regex);
        if (memos.isEmpty()) {
            throw new MemoNotFoundException("존재하지 않는 메모 regex 입니다.");
        }
        return memos;
    }

    default List<Memo> getByTagsContaining(String tagId) {
        List<Memo> memos = findByTagIdsContaining(tagId);
        if (memos.isEmpty()) {
            throw new MemoNotFoundException("존재하지 않는 메모 tag id 입니다.");
        }
        return memos;
    }
}
