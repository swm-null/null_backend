package com.example.memo.memo;

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
import com.example.memo.memo.service.MemoService;
import com.example.memo.memo.service.TagService;
import com.example.memo.memo.service.client.AiMemoClient;
import com.example.memo.memo.service.client.models.AiCreateResponse;
import com.example.memo.memo.service.client.models.AiSearchResponse;
import com.example.memo.memo.service.exception.MemoNotFoundException;
import com.example.memo.memo.service.models.Memo;
import com.example.memo.memo.service.models.Tag;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemoTagService {

    private final AiMemoClient aiMemoClient;
    private final MemoService memoService;
    private final TagService tagService;

    public List<MemoResponse> getAllMemos() {
        List<MemoResponse> memoResponses = new ArrayList<>();
        List<Memo> memos = memoService.getAllMemos();
        for (Memo memo : memos) {
            List<Tag> tags = tagService.getAllTagsById(memo.getTagIds());
            MemoResponse memoResponse = MemoResponse.from(memo, tags);
            memoResponses.add(memoResponse);
        }
        return memoResponses;
    }

    public CreateMemoResponse createMemo(CreateMemoRequest createMemoRequest) {
        AiCreateResponse aiCreateResponse = aiMemoClient.createMemo(createMemoRequest.content());

        Memo memo = createMemoRequest.toMemo(aiCreateResponse.memoEmbeddings());
        Memo savedMemo = memoService.saveMemo(memo);

        List<Tag> tags = new ArrayList<>();

        // 이미 존재하는 태그들 : 필드에 메모 id 추가
        for (String tagId : aiCreateResponse.existingTagIds()) {
            Tag existingTag = tagService.getTagById(tagId);
            existingTag.addMemoId(savedMemo.getId());
            Tag savedTag = tagService.saveTag(existingTag);
            tags.add(savedTag);
        }

        // 새로운 태그들 : 필드에 메모 id 추가, 메모에 tag ids 추가
        List<String> tagIds = new ArrayList<>(aiCreateResponse.existingTagIds());
        for (AiCreateResponse.InnerTag tag : aiCreateResponse.newTags()) {
            Tag newTag = Tag.builder()
                .name(tag.name())
                .memoIds(List.of(savedMemo.getId()))
                .embedding(tag.embedding())
                .build();
            Tag savedTag = tagService.saveTag(newTag);
            tagIds.add(savedTag.getId());
            tags.add(savedTag);
        }
        savedMemo.updateTags(tagIds);
        memoService.saveMemo(savedMemo);
        return CreateMemoResponse.from(savedMemo, tags);
    }

    public SearchMemoResponse searchMemo(SearchMemoRequest searchMemoRequest) {
        AiSearchResponse aiSearchResponse = aiMemoClient.searchMemo(searchMemoRequest.content());
        List<Memo> memos;
        switch (aiSearchResponse.type()) {
            case "similarity" -> memos = memoService.getAllMemosByIds(aiSearchResponse.ids());
            case "regex" -> memos = memoService.getAllMemosContainingRegex(aiSearchResponse.regex());
            case "tag" -> memos = memoService.getAllMemosContainingTagIds(aiSearchResponse.tagIds());
            default -> throw new MemoNotFoundException("메모를 찾지 못했습니다.");
        }

        List<List<Tag>> tags = memos.stream()
            .map(memo -> tagService.getAllTagsById(memo.getTagIds()))
            .toList();

        return SearchMemoResponse.from(aiSearchResponse.processedMessage(), memos, tags);
    }

    public UpdateMemoResponse updateMemo(String memoId, UpdateMemoRequest updateMemoRequest) {
        AiCreateResponse aiCreateResponse = aiMemoClient.createMemo(updateMemoRequest.content());

        Memo memo = memoService.getMemoById(memoId);
        memo.update(updateMemoRequest.content(), aiCreateResponse.memoEmbeddings());
        Memo updatedMemo = memoService.saveMemo(memo);

        List<Tag> tags = updatedMemo.getTagIds().stream()
            .map(tagService::getTagById)
            .toList();

        return UpdateMemoResponse.from(updatedMemo, tags);
    }

    public void deleteMemo(String memoId) {
        Memo memo = memoService.getMemoById(memoId);

        for (String tagId : memo.getTagIds()) {
            Tag tag = tagService.getTagById(tagId);
            tag.deleteMemoId(memoId);
            if (tag.getMemoIds().isEmpty()) {
                tagService.deleteTag(tag);
            } else {
                tagService.saveTag(tag);
            }
        }
        memoService.deleteMemo(memo);
    }
}
