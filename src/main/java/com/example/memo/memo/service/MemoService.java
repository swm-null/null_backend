package com.example.memo.memo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.memo.memo.models.CreateMemoRequest;
import com.example.memo.memo.models.CreateMemoResponse;
import com.example.memo.memo.models.SearchMemoRequest;
import com.example.memo.memo.models.SearchMemoResponse;
import com.example.memo.memo.models.UpdateMemoRequest;
import com.example.memo.memo.models.UpdateMemoResponse;
import com.example.memo.memo.service.exception.MemoNotFoundException;
import com.example.memo.memo.service.models.AiSaveResponse;
import com.example.memo.memo.service.models.AiSearchResponse;
import com.example.memo.memo.service.models.Memo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemoService {

    private final MemoRepository memoRepository;
    private final AiMemoClient aiMemoClient;

    public CreateMemoResponse createMemo(CreateMemoRequest createMemoRequest) {
        AiSaveResponse aiSaveResponse = aiMemoClient.getTags(createMemoRequest.content());
        Memo memo = createMemoRequest.toMemo(
            aiSaveResponse.memoId(),
            aiSaveResponse.tags()
        );
        Memo savedMemo = memoRepository.save(memo);
        return CreateMemoResponse.from(savedMemo);
    }

    public SearchMemoResponse searchMemo(SearchMemoRequest searchMemoRequest) {
        AiSearchResponse aiSearchResponse = aiMemoClient.searchMemo(searchMemoRequest.content());
        List<Memo> memos = new ArrayList<>();
        switch (aiSearchResponse.type()) {
            case "similarity" -> memos.addAll(searchMemoByIdList(aiSearchResponse.ids()));
            case "tag" -> memos.addAll(searchMemoByRegex(aiSearchResponse.regex()));
            case "regex" -> memos.addAll(searchMemoByTag(aiSearchResponse.tags()));
            default -> throw new MemoNotFoundException("메모를 찾지 못했습니다.");
        }
        return SearchMemoResponse.from(aiSearchResponse.processedMessage(), memos);
    }

    public UpdateMemoResponse updateMemo(String memoId, UpdateMemoRequest updateMemoRequest) {
        Memo memo = memoRepository.getById(memoId);
        memo.update(
            updateMemoRequest.content(),
            updateMemoRequest.tags()
        );
        Memo updatedMemo = memoRepository.save(memo);
        return UpdateMemoResponse.from(updatedMemo);
    }

    public void deleteMemo(String memoId) {
        Memo memo = memoRepository.getById(memoId);
        memoRepository.delete(memo);
    }

    private List<Memo> searchMemoByIdList(List<String> ids) {
        return ids.stream()
            .map(memoRepository::getById)
            .collect(Collectors.toList());
    }

    private List<Memo> searchMemoByRegex(String regex) {
        return memoRepository.getByContentRegex(regex);
    }

    private List<Memo> searchMemoByTag(List<String> tags) {
        return tags.stream()
            .flatMap(tag -> memoRepository.getByTagsContaining(tag).stream())
            .collect(Collectors.toList());
    }
}
