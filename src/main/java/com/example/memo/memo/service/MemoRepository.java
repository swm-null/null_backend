package com.example.memo.memo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.memo.memo.service.exception.MemoNotFoundException;
import com.example.memo.memo.service.models.Memo;

public interface MemoRepository extends MongoRepository<Memo, String> {

    @Query("{ 'content' : { $regex: ?0, $options: 'i' } }")
    List<Memo> findByContentRegex(String regex);

    List<Memo> findByTagsContaining(String tag);

    default Memo getById(String id) {
        return findById(id)
            .orElseThrow(() -> new MemoNotFoundException("존재하지 않는 메모 id 입니다."));
    };

    default List<Memo> getByContentRegex(String regex) {
        List<Memo> memos = findByContentRegex(regex);
        if (memos.isEmpty()) {
            throw new MemoNotFoundException("존재하지 않는 메모 regex 입니다.");
        }
        return memos;
    };

    default List<Memo> getByTagsContaining(String tag) {
        List<Memo> memos = findByTagsContaining(tag);
        if (memos.isEmpty()) {
            throw new MemoNotFoundException("존재하지 않는 메모 tag 입니다.");
        }
        return memos;
    };
}
