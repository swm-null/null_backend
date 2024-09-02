package com.example.oatnote.memoTag;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.oatnote.memoTag.models.CreateMemoTagsRequest;
import com.example.oatnote.memoTag.models.CreateMemoTagsResponse;
import com.example.oatnote.memoTag.models.CreateMemosTagsRequest;
import com.example.oatnote.memoTag.models.CreateTagRequest;
import com.example.oatnote.memoTag.models.CreateTagResponse;
import com.example.oatnote.memoTag.models.InnerResponse.MemoTagsResponse;
import com.example.oatnote.memoTag.models.InnerResponse.TagResponse;
import com.example.oatnote.memoTag.models.SearchMemoRequest;
import com.example.oatnote.memoTag.models.SearchMemoResponse;
import com.example.oatnote.memoTag.models.UpdateMemoRequest;
import com.example.oatnote.memoTag.models.UpdateMemoResponse;
import com.example.oatnote.memoTag.models.UpdateTagRequest;
import com.example.oatnote.memoTag.models.UpdateTagResponse;
import com.example.oatnote.memoTag.service.client.AiMemoTagClient;
import com.example.oatnote.memoTag.service.client.models.AiCreateKakaoMemosResponse;
import com.example.oatnote.memoTag.service.client.models.AiCreateMemoResponse;
import com.example.oatnote.memoTag.service.client.models.AiCreateTagResponse;
import com.example.oatnote.memoTag.service.client.models.AiSearchMemoResponse;
import com.example.oatnote.memoTag.service.memo.MemoService;
import com.example.oatnote.memoTag.service.memo.exception.MemoNotFoundException;
import com.example.oatnote.memoTag.service.memo.models.Memo;
import com.example.oatnote.memoTag.service.memoTagRelation.MemoTagRelationService;
import com.example.oatnote.memoTag.service.tag.TagService;
import com.example.oatnote.memoTag.service.tag.models.Tag;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemoTagService {

    private final AiMemoTagClient aiMemoTagClient;
    private final MemoService memoService;
    private final TagService tagService;
    private final MemoTagRelationService memoTagRelationService;

    public CreateMemoTagsResponse createMemoTags(CreateMemoTagsRequest createMemoTagsRequest) {
        AiCreateMemoResponse aiCreateMemoResponse = aiMemoTagClient.createMemo(createMemoTagsRequest.content());

        Memo memo = createMemoTagsRequest.toMemo(aiCreateMemoResponse.memoEmbeddings());
        Memo savedMemo = memoService.saveMemo(memo);

        List<Tag> tags = processTags(
            savedMemo.getId(),
            aiCreateMemoResponse.existingTagIds(),
            aiCreateMemoResponse.newTags()
        );
        return CreateMemoTagsResponse.from(savedMemo, tags);
    }

    public List<CreateMemoTagsResponse> createMemosTags(CreateMemosTagsRequest createMemosTagsRequest) {
        AiCreateKakaoMemosResponse aiCreateKakaoMemosResponse
            = aiMemoTagClient.createKakaoMemos(createMemosTagsRequest.content());

        List<CreateMemoTagsResponse> createMemoTagsRespons = new ArrayList<>();
        for (AiCreateMemoResponse aiCreateMemoResponse : aiCreateKakaoMemosResponse.kakao()) {
            Memo memo = Memo.builder()
                .content(aiCreateMemoResponse.content())
                .embedding(aiCreateMemoResponse.memoEmbeddings())
                .createdAt(aiCreateMemoResponse.timestamp())
                .updatedAt(aiCreateMemoResponse.timestamp())
                .build();
            Memo savedMemo = memoService.saveMemo(memo);
            List<Tag> tags = processTags(
                savedMemo.getId(),
                aiCreateMemoResponse.existingTagIds(),
                aiCreateMemoResponse.newTags()
            );
            createMemoTagsRespons.add(CreateMemoTagsResponse.from(savedMemo, tags));
        }
        return createMemoTagsRespons;
    }

    private List<Tag> processTags(String memoId, List<String> existingTagIds, List<AiCreateMemoResponse.Tag> newTags) {
        List<Tag> tags = new ArrayList<>();

        // 이미 존재하는 태그 처리
        for (String tagId : existingTagIds) {
            Tag existingTag = tagService.getTag(tagId);
            memoTagRelationService.createRelation(memoId, existingTag.getId());
            tags.add(existingTag);
        }

        // 새로운 태그 생성 및 처리
        for (AiCreateMemoResponse.Tag newTag : newTags) {
            Tag tag = com.example.oatnote.memoTag.service.tag.models.Tag.builder()
                .id(newTag.id())
                .name(newTag.name())
                .parentTagId(newTag.parent())
                .childTagIds(new ArrayList<>())
                .embedding(newTag.embedding())
                .build();
            Tag savedTag = tagService.saveTag(tag);
            updateParentTag(newTag.parent(), savedTag.getId());
            memoTagRelationService.createRelation(memoId, savedTag.getId());
            tags.add(savedTag);
        }

        return tags;
    }

    private void updateParentTag(String parentTagId, String childTagId) {
        if (parentTagId != null) {
            Tag parentTag = tagService.getTag(parentTagId);
            parentTag.addChildTagId(childTagId);
            tagService.saveTag(parentTag);
        }
    }

    public List<MemoTagsResponse> getAllMemos() {
        List<MemoTagsResponse> memoTagsRespons = new ArrayList<>();
        List<Memo> memos = memoService.getAllMemos();
        return getMemoResponses(memoTagsRespons, memos);
    }

    public List<MemoTagsResponse> getMemos(String tagId) {
        Set<String> allMemoIdsSet = new HashSet<>();
        collectMemoIds(tagId, allMemoIdsSet);
        List<Memo> memos = memoService.getMemos(new ArrayList<>(allMemoIdsSet));
        return getMemoResponses(new ArrayList<>(), memos);
    }

    private void collectMemoIds(String tagId, Set<String> allMemoIdsSet) {
        List<String> memoIds = memoTagRelationService.getMemoIds(tagId);
        allMemoIdsSet.addAll(memoIds);
        Tag tag = tagService.getTag(tagId);
        for (String childTagId : tag.getChildTagIds()) {
            collectMemoIds(childTagId, allMemoIdsSet);
        }
    }

    private List<MemoTagsResponse> getMemoResponses(List<MemoTagsResponse> memoTagsRespons, List<Memo> memos) {
        for (Memo memo : memos) {
            List<String> tagIds = memoTagRelationService.getTagIds(memo.getId());
            List<Tag> tags = tagService.getTags(tagIds);
            MemoTagsResponse memoTagsResponse = MemoTagsResponse.from(memo, tags);
            memoTagsRespons.add(memoTagsResponse);
        }
        return memoTagsRespons;
    }

    public SearchMemoResponse searchMemosTags(SearchMemoRequest searchMemoRequest) {
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
        memo.update(updateMemoRequest.content(), updateMemoRequest.imageUrls(), aiCreateMemoResponse.memoEmbeddings());
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

    public List<TagResponse> getChildMemosTags(String tagId) {
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
