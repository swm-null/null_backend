package com.example.memo.memo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.memo.memo.service.exception.MemoNotFoundException;
import com.example.memo.memo.service.models.Memo;

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

    public Memo getMemoById(String memoId) {
        return memoRepository.findById(memoId)
            .orElseThrow(() -> new MemoNotFoundException("메모를 찾지 못했습니다: " + memoId));
    }

    public List<Memo> getAllMemosByIds(List<String> memoIds) {
        return memoIds.stream()
            .map(this::getMemoById)
            .toList();
    }

    public List<Memo> getAllMemosContainingRegex(String regex) {
        List<Memo> memos = memoRepository.findAllByContentRegex(regex);
        if (memos.isEmpty()) {
            throw new MemoNotFoundException("존재하지 않는 메모 regex 입니다.");
        }
        return memos;
    }

    public List<Memo> getAllMemosContainingTagIds(List<String> tagIds) {
        List<Memo> memos = memoRepository.findAllByTagIdsIn(tagIds);
        if (memos.isEmpty()) {
            throw new MemoNotFoundException("존재하지 않는 태그 id 입니다.");
        }
        return memos;
    }

    public void deleteMemo(Memo memo) {
        memoRepository.delete(memo);
    }
}
