package com.example.oatnote.memoTag;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.oatnote.memoTag.dto.ChildMemosTagsResponse;
import com.example.oatnote.memoTag.dto.CreateMemoTagsRequest;
import com.example.oatnote.memoTag.dto.CreateMemoTagsResponse;
import com.example.oatnote.memoTag.dto.CreateMemosTagsRequest;
import com.example.oatnote.memoTag.dto.CreateTagRequest;
import com.example.oatnote.memoTag.dto.CreateTagResponse;
import com.example.oatnote.memoTag.dto.MemosTagsResponse;
import com.example.oatnote.memoTag.dto.RootMemosTagsResponse;
import com.example.oatnote.memoTag.dto.SearchMemoRequest;
import com.example.oatnote.memoTag.dto.SearchMemoResponse;
import com.example.oatnote.memoTag.dto.UpdateMemoRequest;
import com.example.oatnote.memoTag.dto.UpdateMemoResponse;
import com.example.oatnote.memoTag.dto.UpdateTagRequest;
import com.example.oatnote.memoTag.dto.UpdateTagResponse;
import com.example.oatnote.memoTag.service.client.AiMemoTagClient;
import com.example.oatnote.memoTag.service.client.models.AiCreateKakaoMemosResponse;
import com.example.oatnote.memoTag.service.client.models.AiCreateMemoTagsResponse;
import com.example.oatnote.memoTag.service.client.models.AiCreateTagResponse;
import com.example.oatnote.memoTag.service.client.models.AiSearchMemoResponse;
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

    private final AiMemoTagClient aiMemoTagClient;
    private final MemoService memoService;
    private final TagService tagService;
    private final MemoTagRelationService memoTagRelationService;
    private final TagsRelationService tagsRelationService;

    private final static boolean IS_LEAF_TAG = true;

    public CreateMemoTagsResponse createMemoTags(CreateMemoTagsRequest createMemoTagsRequest) {
        AiCreateMemoTagsResponse aiCreateMemoTagsResponse = aiMemoTagClient.createMemo(createMemoTagsRequest.content());

        Memo savedMemo = null;
        List<Tag> tags = new ArrayList<>();
        for (var aiMemoTagsResponse : aiCreateMemoTagsResponse.processedMemos()) {
            Memo memo = createMemoTagsRequest.toMemo(aiMemoTagsResponse.embedding());
            savedMemo = memoService.saveMemo(memo);

            for (var newTag : aiMemoTagsResponse.newTags()) {
                Tag tag = Tag.builder()
                    .id(newTag.id())
                    .name(newTag.name())
                    .embedding(newTag.embedding())
                    .build();
                tagService.saveTag(tag);
            }
            for (var parentTagId : aiMemoTagsResponse.parentTagIds()) {
                memoTagRelationService.createRelation(savedMemo.getId(), parentTagId, IS_LEAF_TAG);
                while ((parentTagId = tagsRelationService.getParentTagId(parentTagId)) != null) {
                    memoTagRelationService.createRelation(savedMemo.getId(), parentTagId, !IS_LEAF_TAG);
                }
            }
            for (var addRelation : aiMemoTagsResponse.tagRelations().added()) {
                tagsRelationService.createRelation(addRelation.parentId(), addRelation.childId());
            }
            for (var deletedRelation : aiMemoTagsResponse.tagRelations().deleted()) {
                tagsRelationService.deleteRelation(deletedRelation.parentId(), deletedRelation.childId());
            }
        }
        return CreateMemoTagsResponse.from(savedMemo, tags);
    }

    /* todo 기획 논의 후 삭제 여부 결정
    public CreateTagResponse createTag(String memoId, CreateTagRequest createTagRequest) {
        AiCreateTagResponse aiCreateTagResponse = aiMemoTagClient.createTag(createTagRequest.name());
        Tag tag = createTagRequest.toTag(aiCreateTagResponse.embedding());
        Tag savedTag = tagService.saveTag(tag);
        memoTagRelationService.createRelation(memoId, savedTag.getId(), IS_LEAF_TAG);
        return CreateTagResponse.from(savedTag);
    }
     */

    public void createMemosTags(CreateMemosTagsRequest createMemosTagsRequest) {
        AiCreateKakaoMemosResponse aiCreateKakaoMemosResponse
            = aiMemoTagClient.createKakaoMemos(createMemosTagsRequest.content());

        List<CreateMemoTagsResponse> createMemoTagsRespons = new ArrayList<>();
        for (AiCreateMemoTagsResponse aiCreateMemoTagsResponse : aiCreateKakaoMemosResponse.kakao()) {
            Memo memo = Memo.builder()
                .content(aiCreateMemoTagsResponse.content())
                .embedding(aiCreateMemoTagsResponse.memoEmbeddings())
                .createdAt(aiCreateMemoTagsResponse.timestamp())
                .updatedAt(aiCreateMemoTagsResponse.timestamp())
                .build();
            Memo savedMemo = memoService.saveMemo(memo);
            List<Tag> tags = processTags(
                savedMemo.getId(),
                aiCreateMemoTagsResponse.existingTagIds(),
                aiCreateMemoTagsResponse.newTags()
            );
            createMemoTagsRespons.add(CreateMemoTagsResponse.from(savedMemo, tags));
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
        AiCreateMemoTagsResponse aiCreateMemoTagsResponse = aiMemoTagClient.createMemo(updateMemoRequest.content());

        Memo memo = memoService.getMemo(memoId);
        memo.update(updateMemoRequest.content(), updateMemoRequest.imageUrls(),
            aiCreateMemoTagsResponse.memoEmbeddings());
        Memo updatedMemo = memoService.saveMemo(memo);

        List<String> tagIds = memoTagRelationService.getTagIds(memo.getId());
        List<Tag> tags = tagService.getTags(tagIds);
        return UpdateMemoResponse.from(updatedMemo, tags);
    }

    public UpdateTagResponse updateTag(String tagId, UpdateTagRequest updateTagRequest) {
        AiCreateTagResponse aiCreateTagResponse = aiMemoTagClient.createTag(updateTagRequest.name());
        Tag tag = tagService.getTag(tagId);
        tag.update(updateTagRequest.name(), aiCreateTagResponse.embedding());
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

    private void updateParentTag(String parentTagId, String childTagId) {
        if (parentTagId != null) {
            Tag parentTag = tagService.getTag(parentTagId);
            parentTag.addChildTagId(childTagId);
            tagService.saveTag(parentTag);
        }
    }
}
