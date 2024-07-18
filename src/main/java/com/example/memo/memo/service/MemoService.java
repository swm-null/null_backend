package com.example.memo.memo.service;

import java.util.ArrayList;
import java.util.List;

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
import com.example.memo.memo.service.models.Tag;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemoService {

    private final MemoRepository memoRepository;
    private final TagRepository tagRepository;
    private final AiMemoClient aiMemoClient;

    public List<MemoResponse> getAllMemos() {
        List<Memo> memos = memoRepository.findAll();
        return memos.stream()
            .map(memo -> {
                List<Tag> tags = tagRepository.findAllById(memo.getTags());
                return MemoResponse.from(memo, tags);
            })
            .toList();
    }

    public CreateMemoResponse createMemo(CreateMemoRequest createMemoRequest) {
        AiCreateResponse aiCreateResponse = aiMemoClient.createMemo(createMemoRequest.content());

        Memo memo = createMemoRequest.toMemo(aiCreateResponse.memoEmbeddings());
        Memo savedMemo = memoRepository.save(memo);

        List<Tag> tags = new ArrayList<>();

        // 이미 존재하는 태그들 : 필드에 메모 id 추가
        for (String tagId : aiCreateResponse.existingTagIds()) {
            Tag existingTag = tagRepository.findById(tagId)
                .orElseThrow(() -> new MemoNotFoundException("태그를 찾지 못했습니다: " + tagId));
            existingTag.addMemoId(savedMemo.getId());
            Tag savedTag = tagRepository.save(existingTag);
            tags.add(savedTag);
        }

        // 새로운 태그들 : 필드에 메모 id 추가, 메모에 tag ids 추가
        List<String> tagIds = new ArrayList<>(aiCreateResponse.existingTagIds());
        for (AiCreateResponse.InnerTag tag : aiCreateResponse.newTags()) {
            Tag newTag = Tag.builder()
                .name(tag.name())
                .memos(List.of(savedMemo.getId()))
                .embedding(tag.embedding())
                .build();
            Tag savedTag = tagRepository.save(newTag);
            tagIds.add(savedTag.getId());
            tags.add(savedTag);
        }
        savedMemo.updateTags(tagIds);
        memoRepository.save(savedMemo);
        return CreateMemoResponse.from(savedMemo, tags);
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

        List<List<Tag>> tags = memos.stream()
            .map(memo -> tagRepository.findAllById(memo.getTags()))
            .toList();

        return SearchMemoResponse.from(aiSearchResponse.processedMessage(), memos, tags);
    }

    public UpdateMemoResponse updateMemo(String memoId, UpdateMemoRequest updateMemoRequest) {
        Memo memo = memoRepository.findById(memoId)
            .orElseThrow(() -> new MemoNotFoundException("메모를 찾지 못했습니다: " + memoId));

        AiCreateResponse aiCreateResponse = aiMemoClient.createMemo(updateMemoRequest.content());

        memo.update(updateMemoRequest.content(), aiCreateResponse.memoEmbeddings());
        Memo updatedMemo = memoRepository.save(memo);

        List<Tag> tags = updatedMemo.getTags().stream()
            .map(tagId -> tagRepository.findById(tagId)
                .orElseThrow(() -> new MemoNotFoundException("태그를 찾지 못했습니다: " + tagId)))
            .toList();

        return UpdateMemoResponse.from(updatedMemo, tags);
    }

    public void deleteMemo(String memoId) {
        Memo memo = memoRepository.findById(memoId)
            .orElseThrow(() -> new MemoNotFoundException("메모를 찾지 못했습니다: " + memoId));

        List<String> tagIds = memo.getTags();
        for (String tagId : tagIds) {
            Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new MemoNotFoundException("태그를 찾지 못했습니다: " + tagId));
            tag.deleteMemoId(memoId);
            if (tag.getMemos().isEmpty()) {
                tagRepository.delete(tag);
            } else {
                tagRepository.save(tag);
            }
        }
        memoRepository.delete(memo);
    }

    private List<Memo> searchMemoByIdList(List<String> ids) {
        return ids.stream()
            .map(memoRepository::getById)
            .toList();
    }

    private List<Memo> searchMemoByRegex(String regex) {
        return memoRepository.getByContentRegex(regex);
    }

    private List<Memo> searchMemoByTag(List<String> tags) {
        return tags.stream()
            .flatMap(tag -> memoRepository.getByTagsContaining(tag).stream())
            .toList();
    }
}
