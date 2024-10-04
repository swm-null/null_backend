package com.example.oatnote.memotag.service.memo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.oatnote.memotag.service.memo.model.Memo;
import com.example.oatnote.web.exception.client.OatDataNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemoService {

    private final MemoRepository memoRepository;

    public Memo createMemo(Memo memo) {
        log.info("메모 생성 - 메모: {} / 유저: {}", memo.getId(), memo.getUserId());
        return memoRepository.insert(memo);
    }

    public Memo getMemo(String memoId, String userId) {
        return memoRepository.findByIdAndUserId(memoId, userId)
            .orElseThrow(() -> OatDataNotFoundException.withDetail("메모를 찾지 못했습니다.", memoId));
    }

    public List<Memo> getMemos(List<String> memoIds, String userId) {
        return memoRepository.findByIdInAndUserId(memoIds, userId);
    }

    public Page<Memo> getPagedMemos(List<String> memoIds, PageRequest pageRequest, String userId) {
        return memoRepository.findByIdInAndUserId(memoIds, pageRequest, userId);
    }

    public List<Memo> getMemosContainingRegex(String regex, String userId) {
        List<Memo> memos = memoRepository.findByContentRegexAndUserId(regex, userId);
        if (memos.isEmpty()) {
            throw OatDataNotFoundException.withDetail("해당 regex 에 맞는 메모를 찾지 못했습니다.", regex);
        }
        return memos;
    }

    public Memo updateMemo(Memo memo) {
        log.info("메모 업데이트 - 메모: {} / 유저: {}", memo.getId(), memo.getUserId());
        memoRepository.findByIdAndUserId(memo.getId(), memo.getUserId())
            .orElseThrow(() -> OatDataNotFoundException.withDetail("메모를 찾지 못했습니다.", memo.getId()));
        return memoRepository.save(memo);
    }

    public void deleteMemo(Memo memo) {
        log.info("메모 삭제 - 메모: {} / 유저: {}", memo.getId(), memo.getUserId());
        memoRepository.delete(memo);
    }

    public void deleteUserAllData(String userId) {
        log.info("메모 전체 삭제 - 유저: {}", userId);
        memoRepository.deleteByUserId(userId);
    }
}
