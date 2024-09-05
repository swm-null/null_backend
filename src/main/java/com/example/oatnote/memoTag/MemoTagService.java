package com.example.oatnote.memoTag;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.oatnote.memoTag.dto.ChildMemosTagsResponse;
import com.example.oatnote.memoTag.dto.CreateMemoTagsRequest;
import com.example.oatnote.memoTag.dto.CreateMemoTagsResponse;
import com.example.oatnote.memoTag.dto.CreateMemosTagsRequest;
import com.example.oatnote.memoTag.dto.MemosTagsResponse;
import com.example.oatnote.memoTag.dto.RootMemosTagsResponse;
import com.example.oatnote.memoTag.dto.SearchMemoRequest;
import com.example.oatnote.memoTag.dto.SearchMemoResponse;
import com.example.oatnote.memoTag.dto.UpdateMemoRequest;
import com.example.oatnote.memoTag.dto.UpdateMemoResponse;
import com.example.oatnote.memoTag.dto.UpdateTagRequest;
import com.example.oatnote.memoTag.dto.UpdateTagResponse;
import com.example.oatnote.memoTag.service.client.AIMemoTagClient;
import com.example.oatnote.memoTag.service.client.models.AICreateMemoTagsResponse;
import com.example.oatnote.memoTag.service.client.models.AICreateMemosTagsResponse;
import com.example.oatnote.memoTag.service.client.models.AICreateMemosTagsResponse.AIMemoTagsResponse;
import com.example.oatnote.memoTag.service.client.models.AICreateEmbeddingResponse;
import com.example.oatnote.memoTag.service.client.models.AISearchMemoResponse;
import com.example.oatnote.memoTag.service.memo.MemoService;
import com.example.oatnote.memoTag.service.memo.exception.MemoNotFoundException;
import com.example.oatnote.memoTag.service.memo.model.Memo;
import com.example.oatnote.memoTag.service.memoTagRelation.MemoTagRelationService;
import com.example.oatnote.memoTag.service.tag.TagService;
import com.example.oatnote.memoTag.service.tag.model.Tag;
import com.example.oatnote.memoTag.service.tagsRelation.TagsRelationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemoTagService {

    private final AIMemoTagClient aiMemoTagClient;
    private final MemoService memoService;
    private final TagService tagService;
    private final MemoTagRelationService memoTagRelationService;
    private final TagsRelationService tagsRelationService;

    private final static boolean IS_LINKED_MEMO_TAG = true;

    public CreateMemoTagsResponse createMemoTags(CreateMemoTagsRequest createMemoTagsRequest) {
        AICreateMemoTagsResponse aiCreateMemoTagsResponse = aiMemoTagClient.createMemoTags(
            createMemoTagsRequest.content()
        );
        Memo savedMemo = saveMemoTags(aiCreateMemoTagsResponse.processedMemos());
        List<Tag> tags = updateMemosTagsRelations(aiCreateMemoTagsResponse.processedMemos(), savedMemo);
        return CreateMemoTagsResponse.from(savedMemo, tags);
    }

    public void createMemosTags(CreateMemosTagsRequest createMemosTagsRequest) {
        AICreateMemosTagsResponse aiCreateMemosTagsResponse = aiMemoTagClient.createMemosTags(
            createMemosTagsRequest.content()
        );
        for (var aiMemoTagsResponse : aiCreateMemosTagsResponse.processedMemos()) {
            Memo savedMemo = saveMemoTags(aiMemoTagsResponse);
            updateMemosTagsRelations(aiMemoTagsResponse, savedMemo);
        }
    }

    public RootMemosTagsResponse getRootMemosTags(Integer page, Integer limit) {

        return null;
    }

    public ChildMemosTagsResponse getChildMemosTags(String tagId, Integer page, Integer limit) {

        return null;
    }

    public MemosTagsResponse getMemos(String tagId, Integer page, Integer limit) {

        return null;
    }

    public SearchMemoResponse searchMemosTags(SearchMemoRequest searchMemoRequest) {
        AISearchMemoResponse aiSearchMemoResponse = aiMemoTagClient.searchMemo(searchMemoRequest.content());
        List<Memo> memos;
        switch (aiSearchMemoResponse.type()) {
            case SIMILARITY -> memos = memoService.getMemos(aiSearchMemoResponse.ids());
            case REGEX -> memos = memoService.getMemosContainingRegex(aiSearchMemoResponse.regex());
            case TAG -> memos = memoService.getMemos(memoTagRelationService.getMemoIds(aiSearchMemoResponse.tags()));
            default -> throw new MemoNotFoundException("메모를 찾지 못했습니다.");
        }
        List<List<Tag>> tagsList = memos.stream()
            .map(memo -> tagService.getTags(memoTagRelationService.getLinkedTagIds(memo.getId())))
            .toList();
        return SearchMemoResponse.from(aiSearchMemoResponse.processedMessage(), memos, tagsList);
    }

    public UpdateMemoResponse updateMemo(String memoId, UpdateMemoRequest updateMemoRequest) {
        AICreateEmbeddingResponse aiCreateEmbeddingResponse = aiMemoTagClient.createEmbedding(
            updateMemoRequest.content()
        );
        Memo memo = memoService.getMemo(memoId);
        memo.update(updateMemoRequest.content(), updateMemoRequest.imageUrls(), aiCreateEmbeddingResponse.embedding());
        Memo updatedMemo = memoService.saveMemo(memo);
        List<String> tagIds = memoTagRelationService.getLinkedTagIds(memo.getId());
        List<Tag> tags = tagService.getTags(tagIds);
        return UpdateMemoResponse.from(updatedMemo, tags);
    }

    public UpdateTagResponse updateTag(String tagId, UpdateTagRequest updateTagRequest) {
        AICreateEmbeddingResponse aiCreateEmbeddingResponse = aiMemoTagClient.createEmbedding(updateTagRequest.name());
        Tag tag = tagService.getTag(tagId);
        tag.update(updateTagRequest.name(), aiCreateEmbeddingResponse.embedding());
        Tag updatedTag = tagService.saveTag(tag);
        return UpdateTagResponse.from(updatedTag);
    }

    public void deleteMemo(String memoId) {
        memoTagRelationService.deleteRelationsByMemoId(memoId);
        Memo memo = memoService.getMemo(memoId);
        memoService.deleteMemo(memo);
    }

    public void deleteTag(String tagId) {
        memoTagRelationService.deleteRelationsByTagId(tagId);
        Tag tag = tagService.getTag(tagId);
        tagService.deleteTag(tag);
    }

    private Memo saveMemoTags(AIMemoTagsResponse aiMemoTagsResponse) {
        Memo memo = Memo.builder()
            .content(aiMemoTagsResponse.content())
            .embedding(aiMemoTagsResponse.embedding())
            .build();
        for (var newTag : aiMemoTagsResponse.newTags()) {
            Tag tag = Tag.builder()
                .id(newTag.id())
                .name(newTag.name())
                .embedding(newTag.embedding())
                .build();
            tagService.saveTag(tag);
        }
        return memoService.saveMemo(memo);
    }

    private List<Tag> updateMemosTagsRelations(AIMemoTagsResponse aiMemoTagsResponse, Memo savedMemo) {
        List<Tag> tags = new ArrayList<>();
        for (var parentTagId : aiMemoTagsResponse.parentTagIds()) {
            tags.add(tagService.getTag(parentTagId));
            memoTagRelationService.createRelation(savedMemo.getId(), parentTagId, IS_LINKED_MEMO_TAG);
            while ((parentTagId = tagsRelationService.getParentTagId(parentTagId)) != null) {
                memoTagRelationService.createRelation(savedMemo.getId(), parentTagId, !IS_LINKED_MEMO_TAG);
            }
        }
        for (var addRelation : aiMemoTagsResponse.tagRelations().added()) {
            tagsRelationService.createRelation(addRelation.parentId(), addRelation.childId());
        }
        for (var deletedRelation : aiMemoTagsResponse.tagRelations().deleted()) {
            tagsRelationService.deleteRelation(deletedRelation.parentId(), deletedRelation.childId());
        }
        return tags;
    }
}
