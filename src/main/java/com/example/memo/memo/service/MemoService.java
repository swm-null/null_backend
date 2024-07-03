package com.example.memo.memo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.memo.memo.service.exception.MemoNotFoundException;
import com.example.memo.memo.service.models.AiSaveResponse;
import com.example.memo.memo.service.models.AiSearchResponse;
import com.example.memo.memo.service.models.Memo;
import com.example.memo.memo.service.models.MemoRequestBridge;
import com.example.memo.memo.service.models.MemoResponseBridge;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemoService {

    private final MemoRepository memoRepository;
    private final AiMemoClient aiMemoClient;

    @Transactional
    public MemoResponseBridge createMemo(MemoRequestBridge memoRequestBridge) {
        AiSaveResponse aiSaveResponse = aiMemoClient.getTags(memoRequestBridge);
        Memo memo = MemoRequestBridge.toMemo(
            aiSaveResponse.memoId(),
            aiSaveResponse.tags(),
            memoRequestBridge.getContent()
        );
        Memo savedMemo = memoRepository.save(memo);
        return MemoResponseBridge.from(savedMemo);
    }

    public List<MemoResponseBridge> searchMemo(MemoRequestBridge memoRequestBridge) {
        AiSearchResponse aiSearchResponse = aiMemoClient.searchMemo(memoRequestBridge.getContent());
        List<MemoResponseBridge> memoResponseBridgeList = new ArrayList<>();
        switch (aiSearchResponse.type()) {
            case 1 -> searchMemoByIdList(aiSearchResponse.content(), memoResponseBridgeList);
            case 2 -> searchMemoByRegex(aiSearchResponse.content().get(0), memoResponseBridgeList);
            case 3 -> searchMemoByTag(aiSearchResponse.content().get(0), memoResponseBridgeList);
            default -> throw new MemoNotFoundException("메모를 찾지 못했습니다.");
        }
        return memoResponseBridgeList;
    }

    private void searchMemoByIdList(List<String> ids, List<MemoResponseBridge> memoResponseBridgeList) {
        ids.stream()
            .map(memoRepository::findById)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(MemoResponseBridge::from)
            .forEach(memoResponseBridgeList::add);
    }

    private void searchMemoByRegex(String regex, List<MemoResponseBridge> memoResponseBridgeList) {
        memoRepository.findByContentRegex(regex).stream()
            .map(MemoResponseBridge::from)
            .forEach(memoResponseBridgeList::add);
    }

    private void searchMemoByTag(String tag, List<MemoResponseBridge> memoResponseBridgeList) {
        memoRepository.findByTagsContaining(tag).stream()
            .map(MemoResponseBridge::from)
            .forEach(memoResponseBridgeList::add);
    }
}
