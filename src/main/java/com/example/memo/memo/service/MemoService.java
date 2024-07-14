package com.example.memo.memo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.example.memo.memo.models.CreateMemoRequest;
import com.example.memo.memo.models.CreateMemoResponse;
import com.example.memo.memo.models.MemoResponse;
import com.example.memo.memo.models.SearchMemoRequest;
import com.example.memo.memo.models.SearchMemoResponse;
import com.example.memo.memo.models.UpdateMemoRequest;
import com.example.memo.memo.models.UpdateMemoResponse;
import com.example.memo.memo.service.exception.MemoNotFoundException;
import com.example.memo.memo.service.models.AiCreateResponse;
import com.example.memo.memo.service.models.AiSearchResponse;
import com.example.memo.memo.service.models.Memo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemoService {

    private final MemoRepository memoRepository;
    private final AiMemoClient aiMemoClient;

    public List<MemoResponse> getAllMemos() {
        return memoRepository.findAll().stream()
            .map(MemoResponse::from)
            .collect(Collectors.toList());
    }

    public CreateMemoResponse createMemo(CreateMemoRequest createMemoRequest) {
        Memo memo = createMemoRequest.toMemo();
        /** TODO
         * 메모 저장
         * AI 태그,임베딩 받기 -> 태그 저장 -> 메모 저장?
         */
        Memo savedMemo = memoRepository.save(memo);
        return CreateMemoResponse.from(savedMemo);
    }

    public SearchMemoResponse searchMemo(SearchMemoRequest searchMemoRequest) {
        AiSearchResponse aiSearchResponse = aiMemoClient.searchMemo(searchMemoRequest.content());
        List<Memo> memos = new ArrayList<>();
        switch (aiSearchResponse.type()) {
            case "similarity" -> memos.addAll(searchMemoByIdList(aiSearchResponse.ids()));
            case "regex" -> memos.addAll(searchMemoByRegex(aiSearchResponse.regex()));
            case "tag" -> memos.addAll(searchMemoByTag(aiSearchResponse.tags()));
            default -> throw new MemoNotFoundException("메모를 찾지 못했습니다.");
        }
        return SearchMemoResponse.from(aiSearchResponse.processedMessage(), memos);
    }

    public UpdateMemoResponse updateMemo(ObjectId memoId, UpdateMemoRequest updateMemoRequest) {
        Memo memo = memoRepository.getById(memoId);
        /** TODO
         * 메모 업데이트
         * 메모와 태그 수정. 태그 수정 시 태그 저장.
         */
        Memo updatedMemo = memoRepository.save(memo);
        return UpdateMemoResponse.from(updatedMemo);
    }

    public void deleteMemo(ObjectId memoId) {
        Memo memo = memoRepository.getById(memoId);
        memoRepository.delete(memo);
    }

    private List<Memo> searchMemoByIdList(List<ObjectId> ids) {
        return ids.stream()
            .map(memoRepository::getById)
            .collect(Collectors.toList());
    }

    private List<Memo> searchMemoByRegex(String regex) {
        return memoRepository.getByContentRegex(regex);
    }

    private List<Memo> searchMemoByTag(List<ObjectId> tags) {
        return tags.stream()
            .flatMap(tag -> memoRepository.getByTagsContaining(tag).stream())
            .collect(Collectors.toList());
    }
}
