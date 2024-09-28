package com.example.oatnote.memotag.service.memo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.oatnote.memotag.service.memo.exception.MemoNotFoundException;
import com.example.oatnote.memotag.service.memo.model.Memo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemoService {

    private final MemoRepository memoRepository;

    public Memo saveMemo(Memo memo) {
        return memoRepository.insert(memo);
    }

    public Memo getMemo(String memoId, String userId) {
        return memoRepository.findByIdAndUserId(memoId, userId)
            .orElseThrow(() -> new MemoNotFoundException("메모를 찾지 못했습니다: " + memoId));
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
            throw new MemoNotFoundException("존재하지 않는 메모 regex 입니다.");
        }
        return memos;
    }

    public Memo updateMemo(Memo memo) {
        return memoRepository.save(memo);
    }

    public void deleteMemo(Memo memo) {
        memoRepository.delete(memo);
    }

    public void deleteUserAllData(String userId) {
        memoRepository.deleteByUserId(userId);
    }
}
