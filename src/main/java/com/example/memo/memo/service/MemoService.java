package com.example.memo.memo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        AiSaveResponse aiSaveResponse = aiMemoClient.getTags(createMemoRequest);
        Memo memo = createMemoRequest.toMemo(
            aiSaveResponse.memoId(),
            aiSaveResponse.tags()
        );
        Memo savedMemo = memoRepository.save(memo);
        return CreateMemoResponse.from(savedMemo);
    }

    public List<SearchMemoResponse> searchMemo(SearchMemoRequest searchMemoRequest) {
        AiSearchResponse aiSearchResponse = aiMemoClient.searchMemo(searchMemoRequest.content());
        List<SearchMemoResponse> searchMemoResponseList = new ArrayList<>();
        switch (aiSearchResponse.type()) {
            case 1 -> searchMemoByIdList(aiSearchResponse.content(), searchMemoResponseList);
            case 2 -> searchMemoByRegex(aiSearchResponse.content().get(0), searchMemoResponseList);
            case 3 -> searchMemoByTag(aiSearchResponse.content().get(0), searchMemoResponseList);
            default -> throw new MemoNotFoundException("메모를 찾지 못했습니다.");
        }
        return searchMemoResponseList;
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

    private void searchMemoByIdList(List<String> ids, List<SearchMemoResponse> searchMemoResponseList) {
        ids.stream()
            .map(memoRepository::findById)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(SearchMemoResponse::from)
            .forEach(searchMemoResponseList::add);
    }

    private void searchMemoByRegex(String regex, List<SearchMemoResponse> searchMemoResponseList) {
        memoRepository.findByContentRegex(regex).stream()
            .map(SearchMemoResponse::from)
            .forEach(searchMemoResponseList::add);
    }

    private void searchMemoByTag(String tag, List<SearchMemoResponse> searchMemoResponseList) {
        memoRepository.findByTagsContaining(tag).stream()
            .map(SearchMemoResponse::from)
            .forEach(searchMemoResponseList::add);
    }
}
