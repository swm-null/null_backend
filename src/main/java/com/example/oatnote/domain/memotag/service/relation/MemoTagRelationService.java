package com.example.oatnote.domain.memotag.service.relation;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.oatnote.domain.memotag.dto.enums.MemoSortOrderTypeEnum;
import com.example.oatnote.domain.memotag.service.relation.model.MemoTagRelation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemoTagRelationService {

    private final MemoTagRelationRepository memoTagRelationRepository;

    public void createRelation(String memoId, String tagId, boolean isLinked, String userId) {
        log.info("메모-태그 릴레이션 생성 - 메모: {} / 태그: {} / 유저: {}", memoId, tagId, userId);
        MemoTagRelation memoTagRelation = new MemoTagRelation(memoId, tagId, isLinked, userId);
        memoTagRelationRepository.insert(memoTagRelation);
    }

    public List<String> getMemoIds(String tagId, String userId, PageRequest pageRequest) {
        return memoTagRelationRepository.findByTagIdAndUserId(tagId, userId, pageRequest).stream()
            .map(MemoTagRelation::getMemoId)
            .toList();
    }

    public List<String> getMemoIds(String tagId, boolean isLinked, String userId, PageRequest pageRequest) {
        return memoTagRelationRepository.findByTagIdAndIsLinkedAndUserId(tagId, isLinked, userId, pageRequest).stream()
            .map(MemoTagRelation::getMemoId)
            .toList();
    }

    public List<String> getLinkedTagIds(String memoId, String userId) {
        return memoTagRelationRepository.findByMemoIdAndIsLinkedTrueAndUserId(memoId, userId).stream()
            .map(MemoTagRelation::getTagId)
            .toList();
    }

    public void deleteRelationsByMemoId(String memoId, String userId) {
        log.info("메모-태그 릴레이션 삭제 - 메모: {} / 유저: {}", memoId, userId);
        memoTagRelationRepository.deleteByMemoIdAndUserId(memoId, userId);
    }

    public void deleteRelationsByTagId(String tagId, String userId) {
        log.info("메모-태그 릴레이션 삭제 - 태그: {} / 유저: {}", tagId, userId);
        memoTagRelationRepository.deleteByTagIdAndUserId(tagId, userId);
    }

    public void deleteUserAllData(String userId) {
        log.info("메모-태그 릴레이션 전체 삭제 - 유저: {}", userId);
        memoTagRelationRepository.deleteByUserId(userId);
    }

    public Integer countMemos(String tagId, String userId) {
        return memoTagRelationRepository.countByTagIdAndUserId(tagId, userId);
    }
}
