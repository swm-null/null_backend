package com.example.oatnote.memotag;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.oatnote.memotag.dto.ChildTagsWithMemosResponse;
import com.example.oatnote.memotag.dto.CreateMemoRequest;
import com.example.oatnote.memotag.dto.CreateMemoResponse;
import com.example.oatnote.memotag.dto.CreateMemosRequest;
import com.example.oatnote.memotag.dto.TagWithMemosResponse;
import com.example.oatnote.memotag.dto.SearchMemoRequest;
import com.example.oatnote.memotag.dto.SearchMemoResponse;
import com.example.oatnote.memotag.dto.UpdateMemoRequest;
import com.example.oatnote.memotag.dto.UpdateMemoResponse;
import com.example.oatnote.memotag.dto.UpdateTagRequest;
import com.example.oatnote.memotag.dto.UpdateTagResponse;
import com.example.oatnote.memotag.dto.innerDto.MemoResponse;
import com.example.oatnote.memotag.service.client.AIMemoTagClient;
import com.example.oatnote.memotag.service.client.dto.AICreateEmbeddingResponse;
import com.example.oatnote.memotag.service.client.dto.AICreateMemoResponse;
import com.example.oatnote.memotag.service.client.dto.AICreateMemosResponse;
import com.example.oatnote.memotag.service.client.dto.AISearchMemoResponse;
import com.example.oatnote.memotag.service.client.dto.innerDto.ProcessedMemoResponse;
import com.example.oatnote.memotag.service.memo.MemoService;
import com.example.oatnote.memotag.service.memo.exception.MemoNotFoundException;
import com.example.oatnote.memotag.service.memo.model.Memo;
import com.example.oatnote.memotag.service.memoTagRelation.MemoTagRelationService;
import com.example.oatnote.memotag.service.tag.TagService;
import com.example.oatnote.memotag.service.tag.edge.model.TagEdge;
import com.example.oatnote.memotag.service.tag.model.Tag;
import com.example.oatnote.web.models.Criteria;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemoTagService {

    private final AIMemoTagClient aiMemoTagClient;
    private final MemoService memoService;
    private final TagService tagService;
    private final MemoTagRelationService memoTagRelationService;

    private final static boolean IS_LINKED_MEMO_TAG = true;

    public CreateMemoResponse createMemoTags(CreateMemoRequest createMemoRequest, String userId) {
        AICreateMemoResponse aiCreateMemoResponse = aiMemoTagClient.createMemoTags(
            createMemoRequest.content(),
            userId
        );
        Memo savedMemo = createMemoTags(aiCreateMemoResponse.processedMemo(), userId);
        List<Tag> tags = updateMemosTagsRelations(aiCreateMemoResponse.processedMemo(), savedMemo, userId);

        TagEdge tagEdge = new TagEdge(
            userId,
            aiCreateMemoResponse.newStructure()
        );
        tagService.createTagEdge(tagEdge);
        return CreateMemoResponse.from(savedMemo, tags);
    }

    public void createMemosTags(CreateMemosRequest createMemosRequest) {
        AICreateMemosResponse aiCreateMemosResponse = aiMemoTagClient.createMemosTags(
            createMemosRequest.content(),
            "70c0d720-fa31-4220-86ff-35163e956bd9" //todo Email userId
        );
        for (var aiMemoTagsResponse : aiCreateMemosResponse.processedMemos()) {
            Memo savedMemo = createMemoTags(aiMemoTagsResponse, "70c0d720-fa31-4220-86ff-35163e956bd9");
            updateMemosTagsRelations(aiMemoTagsResponse, savedMemo, "70c0d720-fa31-4220-86ff-35163e956bd9");
        }

        TagEdge tagEdge = new TagEdge(
            "70c0d720-fa31-4220-86ff-35163e956bd9",
            aiCreateMemosResponse.newStructure()
        );
        tagService.createTagEdge(tagEdge);
    }

    public ChildTagsWithMemosResponse getChildTagsWithMemos(
        String parentTagId,
        Integer tagPage,
        Integer tagLimit,
        Integer memoPage,
        Integer memoLimit,
        String userId
    ) {
        if (parentTagId == null) {
            parentTagId = userId;
        }
        Tag parentTag = tagService.getTag(parentTagId, userId);
        List<String> childTagsIds = tagService.getChildTagsIds(parentTag.getId());
        List<Tag> childTags = tagService.getTags(childTagsIds, userId);

        Integer total = childTags.size();
        Criteria criteria = Criteria.of(tagPage, tagLimit, total);
        PageRequest pageRequest = PageRequest.of(
            criteria.getPage(),
            criteria.getLimit(),
            Sort.by(Sort.Direction.DESC, "uTime")
        );
        Page<Tag> result = tagService.getPagedTags(childTagsIds, pageRequest, userId);
        Page<TagWithMemosResponse> pagedMemosTags = result.map(
            tag -> getMemos(tag.getId(), memoPage, memoLimit, userId)
        );
        return ChildTagsWithMemosResponse.from(childTags, pagedMemosTags, criteria);
    }

    public TagWithMemosResponse getMemos(String tagId, Integer memoPage, Integer memoLimit, String userId) {
        Tag tag = tagService.getTag(tagId, userId);
        Integer total = memoTagRelationService.countMemos(tagId);
        Criteria criteria = Criteria.of(memoPage, memoLimit, total);
        PageRequest pageRequest = PageRequest.of(
            criteria.getPage(),
            criteria.getLimit(),
            Sort.by(Sort.Direction.DESC, "uTime")
        );
        Page<Memo> result = memoService.getPagedMemos(memoTagRelationService.getMemoIds(tagId), pageRequest, userId);
        Page<MemoResponse> memoTagsPage = result.map(
            memo -> MemoResponse.from(
                memo,
                tagService.getTags(memoTagRelationService.getLinkedTagIds(memo.getId()), userId)
            )
        );
        return TagWithMemosResponse.from(tag, memoTagsPage, criteria);
    }

    public SearchMemoResponse searchMemosTags(SearchMemoRequest searchMemoRequest, String userId) {
        AISearchMemoResponse aiSearchMemoResponse = aiMemoTagClient.searchMemo(searchMemoRequest.content(), userId);
        List<Memo> memos;
        switch (aiSearchMemoResponse.type()) {
            case SIMILARITY -> memos = memoService.getMemos(aiSearchMemoResponse.ids(), userId);
            case REGEX -> memos = memoService.getMemosContainingRegex(aiSearchMemoResponse.regex(), userId);
            case TAG -> memos = memoService.getMemos(
                memoTagRelationService.getMemoIds(aiSearchMemoResponse.tags()),
                userId
            );
            default -> throw new MemoNotFoundException("메모를 찾지 못했습니다.");
        }
        List<List<Tag>> tagsList = memos.stream()
            .map(memo -> tagService.getTags(memoTagRelationService.getLinkedTagIds(memo.getId()), userId))
            .toList();
        return SearchMemoResponse.from(aiSearchMemoResponse.processedMessage(), memos, tagsList);
    }

    public UpdateMemoResponse updateMemo(String memoId, UpdateMemoRequest updateMemoRequest, String userId) {
        AICreateEmbeddingResponse aiCreateEmbeddingResponse = aiMemoTagClient.createEmbedding(
            updateMemoRequest.content()
        );
        Memo memo = memoService.getMemo(memoId, userId);
        memo.update(updateMemoRequest.content(), updateMemoRequest.imageUrls(), aiCreateEmbeddingResponse.embedding());
        Memo updatedMemo = memoService.updateMemo(memo);
        List<String> tagIds = memoTagRelationService.getLinkedTagIds(memo.getId());
        List<Tag> tags = tagService.getTags(tagIds, userId);
        return UpdateMemoResponse.from(updatedMemo, tags);
    }

    public UpdateTagResponse updateTag(String tagId, UpdateTagRequest updateTagRequest, String userId) {
        AICreateEmbeddingResponse aiCreateEmbeddingResponse = aiMemoTagClient.createEmbedding(updateTagRequest.name());
        Tag tag = tagService.getTag(tagId, userId);
        tag.update(updateTagRequest.name(), aiCreateEmbeddingResponse.embedding());
        Tag updatedTag = tagService.saveTag(tag);
        return UpdateTagResponse.from(updatedTag);
    }

    public void deleteMemo(String memoId, String userId) {
        memoTagRelationService.deleteRelationsByMemoId(memoId);
        Memo memo = memoService.getMemo(memoId, userId);
        memoService.deleteMemo(memo);
    }

    public void deleteTag(String tagId, String userId) {
        memoTagRelationService.deleteRelationsByTagId(tagId);
        Tag tag = tagService.getTag(tagId, userId);
        tagService.deleteTag(tag);
    }

    private Memo createMemoTags(ProcessedMemoResponse aiMemoTagsResponse, String userId) {
        Memo memo = new Memo(
            aiMemoTagsResponse.content(),
            new ArrayList<>(),
            userId,
            aiMemoTagsResponse.embedding()
        );
        Memo createdMemo = memoService.saveMemo(memo);
        for (var newTag : aiMemoTagsResponse.newTags()) {
            Tag tag = new Tag(
                newTag.id(),
                newTag.name(),
                userId,
                newTag.embedding()
            );
            tagService.saveTag(tag);
        }
        return createdMemo;
    }

    private List<Tag> updateMemosTagsRelations(
        ProcessedMemoResponse aiMemoTagsResponse,
        Memo savedMemo,
        String userId
    ) {
        List<Tag> tags = new ArrayList<>();
        for (var linkedTagId : aiMemoTagsResponse.parentTagIds()) {
            tags.add(tagService.getTag(linkedTagId, userId));
            memoTagRelationService.createRelation(savedMemo.getId(), linkedTagId, IS_LINKED_MEMO_TAG);
            List<String> parentTagIds = tagService.getParentTagsIds(linkedTagId);
            createParentTagsRelations(savedMemo.getId(), parentTagIds);
        }
        for (var addRelation : aiMemoTagsResponse.tagsRelations().added()) {
            tagService.createRelation(addRelation.parentId(), addRelation.childId());
        }
        for (var deletedRelation : aiMemoTagsResponse.tagsRelations().deleted()) {
            tagService.deleteRelation(deletedRelation.parentId(), deletedRelation.childId());
        }
        return tags;
    }

    private void createParentTagsRelations(String memoId, List<String> parentTagIds) {
        if (parentTagIds != null && !parentTagIds.isEmpty()) {
            for (var tagId : parentTagIds) {
                memoTagRelationService.createRelation(memoId, tagId, !IS_LINKED_MEMO_TAG);
                createParentTagsRelations(memoId, tagService.getParentTagsIds(tagId));
            }
        }
    }
}
