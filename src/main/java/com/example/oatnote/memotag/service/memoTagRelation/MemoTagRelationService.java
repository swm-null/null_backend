package com.example.oatnote.memotag.service.memoTagRelation;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.oatnote.memotag.service.memoTagRelation.model.MemoTagRelation;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemoTagRelationService {

    private final MemoTagRelationRepository memoTagRelationRepository;

    public void createRelation(String memoId, String tagId, boolean isLinked, String userId) {
        MemoTagRelation memoTagRelation = new MemoTagRelation(memoId, tagId, isLinked, userId);
        memoTagRelationRepository.save(memoTagRelation);
    }

    public List<String> getMemoIds(String tagId) {
        return memoTagRelationRepository.findByTagId(tagId).stream()
            .map(MemoTagRelation::getMemoId)
            .toList();
    }

    public List<String> getMemoIds(List<String> tagIds) {
        return tagIds.stream()
            .flatMap(tagId -> getMemoIds(tagId).stream())
            .toList();
    }

    public List<String> getLinkedTagIds(String memoId) {
        return memoTagRelationRepository.findByMemoIdAndIsLinkedTrue(memoId).stream()
            .map(MemoTagRelation::getTagId)
            .toList();
    }

    public void deleteRelationsByMemoId(String memoId) {
        memoTagRelationRepository.deleteByMemoId(memoId);
    }

    public void deleteRelationsByTagId(String tagId) {
        memoTagRelationRepository.deleteByTagId(tagId);
    }

    public Integer countMemos(String tagId) {
        return memoTagRelationRepository.countByTagId(tagId);
    }

    public void deleteAllUserData(String userId) {
        memoTagRelationRepository.deleteByUserId(userId);
    }
}
