package com.example.oatnote.memoTag.service.relation;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.oatnote.memoTag.service.relation.models.MemoTagRelation;

public interface MemoTagRelationRepository extends MongoRepository<MemoTagRelation, String> {

    List<MemoTagRelation> findByTagId(String tagId);

    List<MemoTagRelation> findByMemoId(String memoId);

    void deleteByMemoId(String memoId);

    void deleteByTagId(String tagId);
}
