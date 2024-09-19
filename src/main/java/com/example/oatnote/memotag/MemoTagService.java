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
import com.example.oatnote.memotag.dto.PagedMemosResponse;
import com.example.oatnote.memotag.dto.PagedTagsResponse;
import com.example.oatnote.memotag.dto.SearchMemoRequest;
import com.example.oatnote.memotag.dto.SearchMemoResponse;
import com.example.oatnote.memotag.dto.UpdateMemoRequest;
import com.example.oatnote.memotag.dto.UpdateMemoResponse;
import com.example.oatnote.memotag.dto.UpdateTagRequest;
import com.example.oatnote.memotag.dto.UpdateTagResponse;
import com.example.oatnote.memotag.dto.enums.SortOrderTypeEnum;
import com.example.oatnote.memotag.dto.innerDto.MemoResponse;
import com.example.oatnote.memotag.dto.innerDto.TagResponse;
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

    public PagedMemosResponse getMemos(
        String tagId,
        Integer memoPage,
        Integer memoLimit,
        SortOrderTypeEnum sortOrder,
        String userId
    ) {
        if (tagId == null) {
            tagId = userId;
        }
        Integer total = memoTagRelationService.countMemos(tagId);
        Criteria criteria = Criteria.of(memoPage, memoLimit, total);
        PageRequest pageRequest = createPageRequest(criteria.getPage(), criteria.getLimit(), sortOrder);

        Page<Memo> result = memoService.getPagedMemos(memoTagRelationService.getMemoIds(tagId), pageRequest, userId);
        Page<MemoResponse> memoTagsPage = result.map(
            memo -> MemoResponse.from(
                memo,
                tagService.getTags(memoTagRelationService.getLinkedTagIds(memo.getId()), userId)
            )
        );
        return PagedMemosResponse.from(memoTagsPage, criteria);
    }

    public List<TagResponse> getChildTags(String parentTagId, String userId) {
        if (parentTagId == null) {
            parentTagId = userId;
        }
        Tag parentTag = tagService.getTag(parentTagId, userId);
        List<Tag> childTags = tagService.getChildTags(parentTag.getId(), userId);
        return childTags.stream()
            .map(TagResponse::from)
            .toList();
    }

    public ChildTagsWithMemosResponse getChildTagsWithMemos(
        String parentTagId,
        Integer tagPage,
        Integer tagLimit,
        Integer memoPage,
        Integer memoLimit,
        SortOrderTypeEnum sortOrder,
        String userId
    ) {
        if (parentTagId == null) {
            parentTagId = userId;
        }
        Tag parentTag = tagService.getTag(parentTagId, userId);
        List<String> childTagsIds = tagService.getChildTagsIds(parentTag.getId());

        Integer total = childTagsIds.size();
        Criteria criteria = Criteria.of(tagPage, tagLimit, total);
        PageRequest pageRequest = createPageRequest(criteria.getPage(), criteria.getLimit(), SortOrderTypeEnum.NAME);

        Page<Tag> result = tagService.getPagedTags(childTagsIds, pageRequest, userId);
        Page<PagedTagsResponse> pagedTags = result.map(
            tag -> new PagedTagsResponse(
                TagResponse.from(tag),
                tagService.getChildTags(tag.getId(), userId).stream()
                    .map(TagResponse::from)
                    .toList()
            )
        );
        Page<PagedMemosResponse> pagedMemos = pagedTags.map(
            pagedTag -> getMemos(pagedTag.tag().id(), memoPage, memoLimit, sortOrder, userId)
        );
        return ChildTagsWithMemosResponse.from(pagedTags, criteria, pagedMemos);
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

    public void deleteAllUserData(String userId) {
        memoTagRelationService.deleteAllUserData(userId);
        memoService.deleteAllUserData(userId);
        tagService.deleteAllUserData(userId);
    }

    Memo createMemoTags(ProcessedMemoResponse aiMemoTagsResponse, String userId) {
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

    List<Tag> updateMemosTagsRelations(
        ProcessedMemoResponse aiMemoTagsResponse,
        Memo savedMemo,
        String userId
    ) {
        List<Tag> tags = new ArrayList<>();
        for (var linkedTagId : aiMemoTagsResponse.parentTagIds()) {
            tags.add(tagService.getTag(linkedTagId, userId));
            memoTagRelationService.createRelation(savedMemo.getId(), linkedTagId, IS_LINKED_MEMO_TAG, userId);
            List<String> parentTagIds = tagService.getParentTagsIds(linkedTagId);
            createParentTagsRelations(savedMemo.getId(), parentTagIds, userId);
        }
        for (var addRelation : aiMemoTagsResponse.tagsRelations().added()) {
            tagService.createRelation(addRelation.parentId(), addRelation.childId(), userId);
        }
        for (var deletedRelation : aiMemoTagsResponse.tagsRelations().deleted()) {
            tagService.deleteRelation(deletedRelation.parentId(), deletedRelation.childId());
        }
        for (var linkedTagId : aiMemoTagsResponse.parentTagIds()) {
            tags.add(tagService.getTag(linkedTagId, userId));
            memoTagRelationService.createRelation(savedMemo.getId(), linkedTagId, IS_LINKED_MEMO_TAG, userId);
            List<String> parentTagIds = tagService.getParentTagsIds(linkedTagId);
            createParentTagsRelations(savedMemo.getId(), parentTagIds, userId);
        }
        return tags;
    }

    void createParentTagsRelations(String memoId, List<String> parentTagIds, String userId) {
        if (parentTagIds != null && !parentTagIds.isEmpty()) {
            for (var tagId : parentTagIds) {
                memoTagRelationService.createRelation(memoId, tagId, !IS_LINKED_MEMO_TAG, userId);
                createParentTagsRelations(memoId, tagService.getParentTagsIds(tagId), userId);
            }
        }
    }

    PageRequest createPageRequest(Integer page, Integer limit, SortOrderTypeEnum sortOrder) {
        Sort sort = switch (sortOrder) {
            case NAME -> Sort.by(Sort.Direction.ASC, "name");
            case OLDEST -> Sort.by(Sort.Direction.ASC, "uTime");
            default -> Sort.by(Sort.Direction.DESC, "uTime");
        };
        return PageRequest.of(page, limit, sort);
    }
}
