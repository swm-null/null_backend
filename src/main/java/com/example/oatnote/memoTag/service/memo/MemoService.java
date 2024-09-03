package com.example.oatnote.memoTag.service.memo;

import java.util.List;

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

    public List<Memo> getAllMemos() {
        return memoRepository.findAll();
    }

    public Memo getMemo(String memoId) {
        return memoRepository.findById(memoId)
            .orElseThrow(() -> new MemoNotFoundException("메모를 찾지 못했습니다: " + memoId));
    }

    public List<Memo> getMemos(List<String> memoIds) {
        return memoRepository.findAllById(memoIds);
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
