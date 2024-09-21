package com.example.oatnote.memotag.service.memoTagRelation;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.oatnote.memotag.service.memoTagRelation.model.MemoTagRelation;

public interface MemoTagRelationRepository extends MongoRepository<MemoTagRelation, String> {

    List<MemoTagRelation> findByMemoIdAndIsLinkedTrue(String memoId);

    List<MemoTagRelation> findByTagId(String tagId);

    void deleteByMemoId(String memoId);

    void deleteByTagId(String tagId);

    Integer countByTagId(String tagId);

    void deleteByUserId(String userId);
}
