package com.example.oatnote.memoTag.service.memoTagRelation;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.oatnote.memoTag.service.memoTagRelation.model.MemoTagRelation;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemoTagRelationService {

    private final MemoTagRelationRepository memoTagRelationRepository;

    public void createRelation(UUID memoId, UUID tagId, boolean isLinked) {
        MemoTagRelation memoTagRelation = new MemoTagRelation(memoId, tagId, isLinked);
        memoTagRelationRepository.save(memoTagRelation);
    }

    public List<UUID> getMemoIds(UUID tagId) {
        return memoTagRelationRepository.findByTagId(tagId).stream()
            .map(MemoTagRelation::getMemoId)
            .toList();
    }

    public List<UUID> getMemoIds(List<UUID> tagIds) {
        return tagIds.stream()
            .flatMap(tagId -> getMemoIds(tagId).stream())
            .toList();
    }

    public List<UUID> getLinkedTagIds(UUID memoId) {
        return memoTagRelationRepository.findByMemoIdAndIsLinkedTrue(memoId).stream()
            .map(MemoTagRelation::getTagId)
            .toList();
    }

    public void deleteRelationsByMemoId(UUID memoId) {
        memoTagRelationRepository.deleteByMemoId(memoId);
    }

    public void deleteRelationsByTagId(UUID tagId) {
        memoTagRelationRepository.deleteByTagId(tagId);
    }

    public Integer countMemos(UUID tagId) {
        return memoTagRelationRepository.countByTagId(tagId);
    }
}
