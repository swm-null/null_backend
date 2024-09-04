package com.example.oatnote.memoTag.service.memoTagRelation;

import java.util.Collection;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.oatnote.memoTag.service.memoTagRelation.model.MemoTagRelation;

public interface MemoTagRelationRepository extends MongoRepository<MemoTagRelation, String> {

    List<MemoTagRelation> findByTagId(String tagId);

    List<MemoTagRelation> findByMemoId(String memoId);

    void deleteByMemoId(String memoId);

    void deleteByTagId(String tagId);

    List<MemoTagRelation> findByMemoIdAndIsLeafTagTrue(String memoId);
}
