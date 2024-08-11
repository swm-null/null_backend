package com.example.oatnote.memo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.oatnote.memo.models.CreateMemoRequest;
import com.example.oatnote.memo.models.CreateMemoResponse;
import com.example.oatnote.memo.models.CreateTagRequest;
import com.example.oatnote.memo.models.CreateTagResponse;
import com.example.oatnote.memo.models.InnerResponse.MemoResponse;
import com.example.oatnote.memo.models.InnerResponse.TagResponse;
import com.example.oatnote.memo.models.SearchMemoRequest;
import com.example.oatnote.memo.models.SearchMemoResponse;
import com.example.oatnote.memo.models.UpdateMemoRequest;
import com.example.oatnote.memo.models.UpdateMemoResponse;
import com.example.oatnote.memo.models.UpdateTagRequest;
import com.example.oatnote.memo.models.UpdateTagResponse;
import com.example.oatnote.memo.service.client.AiMemoTagClient;
import com.example.oatnote.memo.service.client.models.AiCreateMemoResponse;
import com.example.oatnote.memo.service.client.models.AiCreateTagResponse;
import com.example.oatnote.memo.service.client.models.AiSearchMemoResponse;
import com.example.oatnote.memo.service.memo.MemoService;
import com.example.oatnote.memo.service.memo.exception.MemoNotFoundException;
import com.example.oatnote.memo.service.memo.models.Memo;
import com.example.oatnote.memo.service.memoTagRelation.MemoTagRelationService;
import com.example.oatnote.memo.service.tag.TagService;
import com.example.oatnote.memo.service.tag.models.Tag;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemoTagService {

    private final AiMemoTagClient aiMemoTagClient;
    private final MemoService memoService;
    private final TagService tagService;
    private final MemoTagRelationService memoTagRelationService;

    public CreateMemoResponse createMemo(CreateMemoRequest createMemoRequest) {
        AiCreateMemoResponse aiCreateMemoResponse = aiMemoTagClient.createMemo(createMemoRequest.content());

        Memo memo = createMemoRequest.toMemo(aiCreateMemoResponse.memoEmbeddings());
        Memo savedMemo = memoService.saveMemo(memo);

        List<Tag> tags = new ArrayList<>();
        // 이미 존재하는 태그들
        for (String tagId : aiCreateMemoResponse.existingTagIds()) {
            Tag existingTag = tagService.getTag(tagId);
            memoTagRelationService.createRelation(savedMemo.getId(), existingTag.getId());
            tags.add(existingTag);
        }
        // 새로운 태그들 생성
        for (AiCreateMemoResponse.InnerTag newTag : aiCreateMemoResponse.newTags()) {
            Tag tag = Tag.builder()
                .id(newTag.id())
                .name(newTag.name())
                .parentTagId(newTag.parent())
                .childTagIds(new ArrayList<>())
                .embedding(newTag.embedding())
                .build();
            Tag savedTag = tagService.saveTag(tag);
            parentTagUpdate(newTag.parent(), savedTag.getId());
            memoTagRelationService.createRelation(savedMemo.getId(), savedTag.getId());
            tags.add(savedTag);
        }
        return CreateMemoResponse.from(savedMemo, tags);
    }

    private void parentTagUpdate(String parentTagId, String childTagId) {
        if (parentTagId != null) {
            Tag parentTag = tagService.getTag(parentTagId);
            parentTag.addChildTagId(childTagId);
            tagService.saveTag(parentTag);
        }
    }

    public List<MemoResponse> getAllMemos() {
        List<MemoResponse> memoResponses = new ArrayList<>();
        List<Memo> memos = memoService.getAllMemos();
        return getMemoResponses(memoResponses, memos);
    }

    public List<MemoResponse> getMemos(String tagId) {
        List<String> memoIds = memoTagRelationService.getMemoIds(tagId);
        List<Memo> memos = memoService.getMemos(memoIds);
        List<MemoResponse> memoResponses = new ArrayList<>();
        return getMemoResponses(memoResponses, memos);
    }

    private List<MemoResponse> getMemoResponses(List<MemoResponse> memoResponses, List<Memo> memos) {
        for (Memo memo : memos) {
            List<String> tagIds = memoTagRelationService.getTagIds(memo.getId());
            List<Tag> tags = tagService.getTags(tagIds);
            MemoResponse memoResponse = MemoResponse.from(memo, tags);
            memoResponses.add(memoResponse);
        }
        return memoResponses;
    }

    public SearchMemoResponse searchMemo(SearchMemoRequest searchMemoRequest) {
        AiSearchMemoResponse aiSearchMemoResponse = aiMemoTagClient.searchMemo(searchMemoRequest.content());
        List<Memo> memos;
        switch (aiSearchMemoResponse.type()) {
            case SIMILARITY -> memos = memoService.getMemos(aiSearchMemoResponse.ids());
            case REGEX -> memos = memoService.getMemosContainingRegex(aiSearchMemoResponse.regex());
            case TAG -> memos = memoService.getMemos(memoTagRelationService.getMemoIds(aiSearchMemoResponse.tags()));
            default -> throw new MemoNotFoundException("메모를 찾지 못했습니다.");
        }

        List<List<Tag>> tagsList = memos.stream()
            .map(memo -> tagService.getTags(memoTagRelationService.getTagIds(memo.getId())))
            .toList();
        return SearchMemoResponse.from(aiSearchMemoResponse.processedMessage(), memos, tagsList);
    }

    public UpdateMemoResponse updateMemo(String memoId, UpdateMemoRequest updateMemoRequest) {
        AiCreateMemoResponse aiCreateMemoResponse = aiMemoTagClient.createMemo(updateMemoRequest.content());

        Memo memo = memoService.getMemo(memoId);
        memo.update(updateMemoRequest.content(), aiCreateMemoResponse.memoEmbeddings());
        Memo updatedMemo = memoService.saveMemo(memo);

        List<String> tagIds = memoTagRelationService.getTagIds(memo.getId());
        List<Tag> tags = tagService.getTags(tagIds);
        return UpdateMemoResponse.from(updatedMemo, tags);
    }

    public void deleteMemo(String memoId) {
        memoTagRelationService.deleteRelationsByMemoId(memoId);
        Memo memo = memoService.getMemo(memoId);
        memoService.deleteMemo(memo);
    }

    public CreateTagResponse createTag(String memoId, CreateTagRequest createTagRequest) {
        AiCreateTagResponse aiCreateTagResponse = aiMemoTagClient.createTag(createTagRequest.name());
        Tag tag = createTagRequest.toTag(aiCreateTagResponse.embedding());
        Tag savedTag = tagService.saveTag(tag);
        memoTagRelationService.createRelation(memoId, savedTag.getId());
        return CreateTagResponse.from(savedTag);
    }

    public List<TagResponse> getAllTags() {
        return tagService.getAllTags().stream()
            .map(TagResponse::from)
            .toList();
    }

    public List<TagResponse> getRootTags() {
        List<Tag> rootTags = tagService.getRootTags();
        return rootTags.stream()
            .map(TagResponse::from)
            .toList();
    }

    public List<TagResponse> getChildTags(String tagId) {
        Tag tag = tagService.getTag(tagId);
        List<Tag> childTags = tagService.getTags(tag.getChildTagIds());
        return childTags.stream()
            .map(TagResponse::from)
            .toList();
    }

    public UpdateTagResponse updateTag(String tagId, UpdateTagRequest updateTagRequest) {
        AiCreateTagResponse aiCreateTagResponse = aiMemoTagClient.createTag(updateTagRequest.name());
        Tag tag = tagService.getTag(tagId);
        tag.update(updateTagRequest.name(), aiCreateTagResponse.embedding());
        Tag updatedTag = tagService.saveTag(tag);
        return UpdateTagResponse.from(updatedTag);
    }

    public void deleteTag(String tagId) {
        memoTagRelationService.deleteRelationsByTagId(tagId);
        Tag tag = tagService.getTag(tagId);
        tagService.deleteTag(tag);
    }
}
