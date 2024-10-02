package com.example.oatnote.memotag.service.memoTagRelation;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.oatnote.memotag.service.memoTagRelation.model.MemoTagRelation;

public interface MemoTagRelationRepository extends MongoRepository<MemoTagRelation, String> {

    List<MemoTagRelation> findByMemoIdAndIsLinkedTrueAndUserId(String memoId, String userId);

    List<MemoTagRelation> findByTagIdAndUserId(String tagId, String userId);

    void deleteByMemoIdAndUserId(String memoId, String userId);

    void deleteByTagIdAndUserId(String tagId, String userId);

    void deleteByUserId(String userId);

    Integer countByTagIdAndUserId(String tagId, String userId);
}
