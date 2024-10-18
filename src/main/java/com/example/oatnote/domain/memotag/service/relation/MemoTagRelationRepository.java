package com.example.oatnote.domain.memotag.service.relation;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.oatnote.domain.memotag.service.relation.model.MemoTagRelation;

public interface MemoTagRelationRepository extends MongoRepository<MemoTagRelation, String> {

    List<MemoTagRelation> findByMemoIdAndIsLinkedTrueAndUserId(String memoId, String userId);

    List<MemoTagRelation> findByTagIdAndUserId(String tagId, String userId, Pageable pageable);

    List<MemoTagRelation> findByTagIdAndIsLinkedAndUserId(
        String tagId,
        boolean isLinked,
        String userId,
        Pageable pageable
    );

    void deleteByMemoIdAndUserId(String memoId, String userId);

    void deleteByTagIdAndUserId(String tagId, String userId);

    void deleteByUserId(String userId);

    Integer countByTagIdAndUserId(String tagId, String userId);
}
