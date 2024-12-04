package com.example.oatnote.memo.service.memo;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.oatnote.memo.service.memo.model.Memo;
import com.example.oatnote.web.controller.exception.client.OatDataNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemoService {

    private final MemoRepository memoRepository;

    public void createMemos(List<Memo> memos, String userId) {
        log.info("메모 리스트 생성 / IDs: {} / 유저: {}", memos.stream().map(Memo::getId).toList(), userId);
        memoRepository.saveAll(memos);
    }

    public Memo getMemo(String memoId, String userId) {
        return memoRepository.findByIdAndUserId(memoId, userId)
            .orElseThrow(() -> OatDataNotFoundException.withDetail("메모를 찾지 못했습니다.", memoId));
    }

    public List<Memo> getMemos(List<String> memoIds, String userId) {
        List<Memo> memos = memoRepository.findByIdInAndUserId(memoIds, userId);
        Map<String, Memo> memoMap = memos.stream()
            .collect(Collectors.toMap(Memo::getId, memo -> memo));
        return memoIds.stream()
            .map(memoMap::get)
            .filter(Objects::nonNull)
            .toList();
    }

    public Page<Memo> getMemos(List<String> memoIds, String userId, Pageable pageable) {
        return memoRepository.findByIdInAndUserId(memoIds, userId, pageable);
    }

    public Memo updateMemo(Memo memo) {
        log.info("메모 업데이트 / 메모: {} / 유저: {}", memo.getId(), memo.getUserId());
        memoRepository.findByIdAndUserId(memo.getId(), memo.getUserId())
            .orElseThrow(() -> OatDataNotFoundException.withDetail("메모를 찾지 못했습니다.", memo.getId()));
        return memoRepository.save(memo);
    }

    public void deleteMemo(String memoId, String userId) {
        log.info("메모 삭제 / 메모: {} / 유저: {}", memoId, userId);
        memoRepository.deleteByIdAndUserId(memoId, userId);
    }

    public void deleteUserAllData(String userId) {
        log.info("메모 전체 삭제 / 유저: {}", userId);
        memoRepository.deleteByUserId(userId);
    }

    public void deleteMemos(List<String> memoIds, String userId) {
        log.info("메모 리스트 삭제 / 메모: {} / 유저: {}", memoIds, userId);
        memoRepository.deleteByIdInAndUserId(memoIds, userId);
    }

    public List<String> getFileUrls(List<String> memoIds, String userId) {
        return memoRepository.findFileUrlsByIdInAndUserId(memoIds, userId).stream()
            .flatMap(memo -> Stream.concat(
                memo.getImageUrls().stream(),
                memo.getVoiceUrls().stream()
            ))
            .toList();
    }
}
