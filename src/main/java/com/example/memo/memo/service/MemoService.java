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

    public List<Memo> getAllMemos() {
        return memoRepository.findAll();
    }

    public Memo saveMemo(Memo memo) {
        return memoRepository.save(memo);
    }

    public Memo getMemoById(String memoId) {
        return memoRepository.findById(memoId)
            .orElseThrow(() -> new MemoNotFoundException("메모를 찾지 못했습니다: " + memoId));
    }

    public void deleteMemo(Memo memo) {
        memoRepository.delete(memo);
    }

    public List<Memo> searchMemoByIdList(List<String> ids) {
        return ids.stream()
            .map(memoRepository::getById)
            .toList();
    }

    public List<Memo> searchMemoByRegex(String regex) {
        return memoRepository.getByContentRegex(regex);
    }

    public List<Memo> searchMemoByTag(List<String> tags) {
        return tags.stream()
            .flatMap(tag -> memoRepository.getByTagsContaining(tag).stream())
            .toList();
    }
}
