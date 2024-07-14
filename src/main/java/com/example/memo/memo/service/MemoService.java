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
import com.example.memo.tag.service.models.Tag;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemoService {

    private final MemoRepository memoRepository;
    private final TagRepository tagRepository;
    private final AiMemoClient aiMemoClient;

    public List<MemoResponse> getAllMemos() {
        return memoRepository.findAll().stream()
            .map(MemoResponse::from)
            .collect(Collectors.toList());
    }

    public CreateMemoResponse createMemo(CreateMemoRequest createMemoRequest) {
        AiCreateResponse aiCreateResponse = aiMemoClient.createMemo(createMemoRequest.content());

        Memo memo = createMemoRequest.toMemo(aiCreateResponse.memoEmbeddings());
        Memo savedMemo = memoRepository.save(memo);

        List<Tag> tags = new ArrayList<>();

        // 이미 존재하는 태그들 : 필드에 메모 id 추가
        for (ObjectId tagId : aiCreateResponse.existingTagIds()) {
            Tag existingTag = tagRepository.findById(tagId)
                .orElseThrow(() -> new MemoNotFoundException("태그를 찾지 못했습니다: " + tagId));
            existingTag.addMemoId(savedMemo.getId());
            Tag savedTag = tagRepository.save(existingTag);

            tags.add(savedTag);
        }

        // 새로운 태그들 : 필드에 메모 id 추가, 메모에 tag ids 추가
        List<ObjectId> tagIds = new ArrayList<>(aiCreateResponse.existingTagIds());
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

    public UpdateMemoResponse updateMemo(ObjectId memoId, UpdateMemoRequest updateMemoRequest) {
        Memo memo = memoRepository.getById(memoId);
        /* TODO 업데이트 기능 논의 필요
        memo.update(updateMemoRequest);

        List<Tag> tags = updateMemoRequest.getTags().stream()
                .map(tagId -> tagRepository.getById(tagId))
                .collect(Collectors.toList());

        memo.setTags(tags.stream().map(Tag::getId).collect(Collectors.toList()));
        Memo updatedMemo = memoRepository.save(memo);
         */
        return UpdateMemoResponse.from(memo);
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
