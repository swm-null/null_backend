package com.example.oatnote.memoTag.service.memo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.oatnote.memoTag.service.memo.exception.MemoNotFoundException;
import com.example.oatnote.memoTag.service.memo.model.Memo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemoService {

    private final MemoRepository memoRepository;

    public Memo saveMemo(Memo memo) {
        return memoRepository.save(memo);
    }

    public Memo getMemo(UUID memoId) {
        return memoRepository.findById(memoId)
            .orElseThrow(() -> new MemoNotFoundException("메모를 찾지 못했습니다: " + memoId));
    }

    public List<Memo> getMemos(List<UUID> memoIds) {
        return memoRepository.findAllById(memoIds);
    }

    public Page<Memo> getPagedMemos(List<UUID> memoIds, PageRequest pageRequest) {
        return memoRepository.findAllByIdIn(memoIds, pageRequest);
    }

    public List<Memo> getMemosContainingRegex(String regex) {
        List<Memo> memos = memoRepository.findByContentRegex(regex);
        if (memos.isEmpty()) {
            throw new MemoNotFoundException("존재하지 않는 메모 regex 입니다.");
        }
        return memos;
    }

    public void deleteMemo(Memo memo) {
        memoRepository.delete(memo);
    }
}
