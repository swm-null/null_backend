package com.example.oatnote.memoTag;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.oatnote.memoTag.dto.ChildMemosTagsResponse;
import com.example.oatnote.memoTag.dto.CreateMemoTagsRequest;
import com.example.oatnote.memoTag.dto.CreateMemoTagsResponse;
import com.example.oatnote.memoTag.dto.CreateMemosTagsRequest;
import com.example.oatnote.memoTag.dto.PagedMemosTagsResponse;
import com.example.oatnote.memoTag.dto.SearchMemoRequest;
import com.example.oatnote.memoTag.dto.SearchMemoResponse;
import com.example.oatnote.memoTag.dto.UpdateMemoRequest;
import com.example.oatnote.memoTag.dto.UpdateMemoResponse;
import com.example.oatnote.memoTag.dto.UpdateTagRequest;
import com.example.oatnote.memoTag.dto.UpdateTagResponse;
import com.example.oatnote.memoTag.dto.innerDto.MemoTagsResponse;
import com.example.oatnote.memoTag.service.client.AIMemoTagClient;
import com.example.oatnote.memoTag.service.client.dto.AICreateEmbeddingResponse;
import com.example.oatnote.memoTag.service.client.dto.AICreateMemoTagsResponse;
import com.example.oatnote.memoTag.service.client.dto.AICreateMemosTagsResponse;
import com.example.oatnote.memoTag.service.client.dto.AISearchMemoResponse;
import com.example.oatnote.memoTag.service.client.dto.innerDto.ProcessedMemoResponse;
import com.example.oatnote.memoTag.service.memo.MemoService;
import com.example.oatnote.memoTag.service.memo.exception.MemoNotFoundException;
import com.example.oatnote.memoTag.service.memo.model.Memo;
import com.example.oatnote.memoTag.service.memoTagRelation.MemoTagRelationService;
import com.example.oatnote.memoTag.service.tag.TagService;
import com.example.oatnote.memoTag.service.tag.model.Tag;
import com.example.oatnote.memoTag.service.tagsRelation.TagsRelationService;
import com.example.oatnote.web.models.Criteria;

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
        Memo savedMemo = createMemoTags(aiCreateMemoTagsResponse.processedMemo());
        List<Tag> tags = updateMemosTagsRelations(aiCreateMemoTagsResponse.processedMemo(), savedMemo);
        return CreateMemoTagsResponse.from(savedMemo, tags);
    }

    public void createMemosTags(CreateMemosTagsRequest createMemosTagsRequest) {
        AICreateMemosTagsResponse aiCreateMemosTagsResponse = aiMemoTagClient.createMemosTags(
            createMemosTagsRequest.content()
        );
        for (var aiMemoTagsResponse : aiCreateMemosTagsResponse.processedMemos()) {
            Memo savedMemo = createMemoTags(aiMemoTagsResponse);
            updateMemosTagsRelations(aiMemoTagsResponse, savedMemo);
        }
    }

    public ChildMemosTagsResponse getChildMemosTags(
        UUID parentTagId,
        Integer tagPage,
        Integer tagLimit,
        Integer memoPage,
        Integer memoLimit
    ) {
        List<UUID> childTagsIds = tagsRelationService.getChildTagsIds(parentTagId);
        List<Tag> childTags = tagService.getTags(childTagsIds);
        Integer total = childTags.size();
        Criteria criteria = Criteria.of(tagPage, tagLimit, total);
        PageRequest pageRequest = PageRequest.of(
            criteria.getPage(),
            criteria.getLimit(),
            Sort.by(Sort.Direction.DESC, "uTime")
        );
        Page<Tag> result = tagService.getPagedTags(childTagsIds, pageRequest);
        Page<PagedMemosTagsResponse> pagedMemosTags = result.map(tag ->
            getMemos(tag.getId(), memoPage, memoLimit)
        );
        return ChildMemosTagsResponse.from(
            childTags,
            pagedMemosTags,
            criteria
        );
    }

    public PagedMemosTagsResponse getMemos(UUID tagId, Integer memoPage, Integer memoLimit) {
        Tag tag = tagService.getTag(tagId);
        Integer total = memoTagRelationService.countMemos(tagId);
        Criteria criteria = Criteria.of(memoPage, memoLimit, total);
        PageRequest pageRequest = PageRequest.of(
            criteria.getPage(),
            criteria.getLimit(),
            Sort.by(Sort.Direction.DESC, "uTime")
        );
        Page<Memo> result = memoService.getPagedMemos(memoTagRelationService.getMemoIds(tagId), pageRequest);
        Page<MemoTagsResponse> memoTagsPage = result.map(memo ->
            MemoTagsResponse.from(
                memo,
                tagService.getTags(memoTagRelationService.getLinkedTagIds(memo.getId()))
            )
        );
        return PagedMemosTagsResponse.from(tag, memoTagsPage, criteria);
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

    public UpdateMemoResponse updateMemo(UUID memoId, UpdateMemoRequest updateMemoRequest) {
        AICreateEmbeddingResponse aiCreateEmbeddingResponse = aiMemoTagClient.createEmbedding(
            updateMemoRequest.content()
        );
        Memo memo = memoService.getMemo(memoId);
        memo.update(updateMemoRequest.content(), updateMemoRequest.imageUrls(), aiCreateEmbeddingResponse.embedding());
        Memo updatedMemo = memoService.saveMemo(memo);
        List<UUID> tagIds = memoTagRelationService.getLinkedTagIds(memo.getId());
        List<Tag> tags = tagService.getTags(tagIds);
        return UpdateMemoResponse.from(updatedMemo, tags);
    }

    public UpdateTagResponse updateTag(UUID tagId, UpdateTagRequest updateTagRequest) {
        AICreateEmbeddingResponse aiCreateEmbeddingResponse = aiMemoTagClient.createEmbedding(updateTagRequest.name());
        Tag tag = tagService.getTag(tagId);
        tag.update(updateTagRequest.name(), aiCreateEmbeddingResponse.embedding());
        Tag updatedTag = tagService.saveTag(tag);
        return UpdateTagResponse.from(updatedTag);
    }

    public void deleteMemo(UUID memoId) {
        memoTagRelationService.deleteRelationsByMemoId(memoId);
        Memo memo = memoService.getMemo(memoId);
        memoService.deleteMemo(memo);
    }

    public void deleteTag(UUID tagId) {
        memoTagRelationService.deleteRelationsByTagId(tagId);
        Tag tag = tagService.getTag(tagId);
        tagService.deleteTag(tag);
    }

    private Memo createMemoTags(ProcessedMemoResponse aiMemoTagsResponse) {
        Memo memo = new Memo(
            aiMemoTagsResponse.content(),
            new ArrayList<>(),
            UUID.fromString("todoUserId"), // todo 유저 아이디
            aiMemoTagsResponse.embedding()
        );
        for (var newTag : aiMemoTagsResponse.newTags()) {
            Tag tag = new Tag(
                newTag.name(),
                UUID.fromString("todoUserId"),
                newTag.embedding()
            );
            tagService.saveTag(tag);
        }
        return memoService.saveMemo(memo);
    }

    private List<Tag> updateMemosTagsRelations(ProcessedMemoResponse aiMemoTagsResponse, Memo savedMemo) {
        List<Tag> tags = new ArrayList<>();
        for (var linkedTagId : aiMemoTagsResponse.parentTagIds()) {
            tags.add(tagService.getTag(linkedTagId));
            memoTagRelationService.createRelation(savedMemo.getId(), linkedTagId, IS_LINKED_MEMO_TAG);
            List<UUID> parentTagIds = tagsRelationService.getParentTagsIds(linkedTagId);
            createParentTagsRelations(savedMemo.getId(), parentTagIds);
        }
        for (var addRelation : aiMemoTagsResponse.tagRelations().added()) {
            tagsRelationService.createRelation(addRelation.parentId(), addRelation.childId());
        }
        for (var deletedRelation : aiMemoTagsResponse.tagRelations().deleted()) {
            tagsRelationService.deleteRelation(deletedRelation.parentId(), deletedRelation.childId());
        }
        return tags;
    }

    private void createParentTagsRelations(UUID memoId, List<UUID> parentTagIds) {
        if (parentTagIds != null && !parentTagIds.isEmpty()) {
            for (var tagId : parentTagIds) {
                memoTagRelationService.createRelation(memoId, tagId, !IS_LINKED_MEMO_TAG);
                createParentTagsRelations(memoId, tagsRelationService.getParentTagsIds(tagId));
            }
        }
    }
}
