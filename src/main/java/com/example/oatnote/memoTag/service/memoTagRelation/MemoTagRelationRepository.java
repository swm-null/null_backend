package com.example.oatnote.memoTag.service.memoTagRelation;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.oatnote.memoTag.service.memoTagRelation.model.MemoTagRelation;

public interface MemoTagRelationRepository extends MongoRepository<MemoTagRelation, UUID> {

    List<MemoTagRelation> findByMemoIdAndIsLinkedTrue(UUID memoId);

    List<MemoTagRelation> findByTagId(UUID tagId);

    void deleteByMemoId(UUID memoId);

    void deleteByTagId(UUID tagId);

    Integer countByTagId(UUID tagId);
}
