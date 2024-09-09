package com.example.oatnote.memoTag;

import java.util.ArrayList;
import java.util.List;

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
import com.example.oatnote.memoTag.service.tagEdge.TagEdgeService;
import com.example.oatnote.memoTag.service.tagEdge.model.TagEdge;
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
    private final TagEdgeService tagEdgeService;

    private final static boolean IS_LINKED_MEMO_TAG = true;

    public CreateMemoTagsResponse createMemoTags(CreateMemoTagsRequest createMemoTagsRequest, String userId) {
        AICreateMemoTagsResponse aiCreateMemoTagsResponse = aiMemoTagClient.createMemoTags(
            createMemoTagsRequest.content(),
            userId
        );
        Memo savedMemo = createMemoTags(aiCreateMemoTagsResponse.processedMemo(), userId);
        List<Tag> tags = updateMemosTagsRelations(aiCreateMemoTagsResponse.processedMemo(), savedMemo, userId);

        TagEdge tagEdge = new TagEdge(
            userId,
            aiCreateMemoTagsResponse.newStructure()
        );
        tagEdgeService.saveTagEdge(tagEdge);
        return CreateMemoTagsResponse.from(savedMemo, tags);
    }

    public void createMemosTags(CreateMemosTagsRequest createMemosTagsRequest) {
        AICreateMemosTagsResponse aiCreateMemosTagsResponse = aiMemoTagClient.createMemosTags(
            createMemosTagsRequest.content(),
            "b973cd66-bc7b-4820-a9e8-78a1edae021c" //todo Email userId
        );
        for (var aiMemoTagsResponse : aiCreateMemosTagsResponse.processedMemos()) {
            Memo savedMemo = createMemoTags(aiMemoTagsResponse, "b973cd66-bc7b-4820-a9e8-78a1edae021c");
            updateMemosTagsRelations(aiMemoTagsResponse, savedMemo, "b973cd66-bc7b-4820-a9e8-78a1edae021c");
        }

        TagEdge tagEdge = new TagEdge(
            "b973cd66-bc7b-4820-a9e8-78a1edae021c",
            aiCreateMemosTagsResponse.newStructure()
        );
        tagEdgeService.saveTagEdge(tagEdge);
    }

    public ChildMemosTagsResponse getChildMemosTags(
        String parentTagName,
        Integer tagPage,
        Integer tagLimit,
        Integer memoPage,
        Integer memoLimit,
        String userId
    ) {
        Tag parentTag = tagService.getTagByName(parentTagName, userId);
        List<String> childTagsIds = tagsRelationService.getChildTagsIds(parentTag.getId());
        List<Tag> childTags = tagService.getTags(childTagsIds, userId);

        Integer total = childTags.size();
        Criteria criteria = Criteria.of(tagPage, tagLimit, total);
        PageRequest pageRequest = PageRequest.of(
            criteria.getPage(),
            criteria.getLimit(),
            Sort.by(Sort.Direction.DESC, "uTime")
        );
        Page<Tag> result = tagService.getPagedTags(childTagsIds, pageRequest, userId);
        Page<PagedMemosTagsResponse> pagedMemosTags = result.map(
            tag -> getMemos(tag.getId(), memoPage, memoLimit, userId)
        );
        return ChildMemosTagsResponse.from(childTags, pagedMemosTags, criteria);
    }

    public PagedMemosTagsResponse getMemos(String tagId, Integer memoPage, Integer memoLimit, String userId) {
        Tag tag = tagService.getTag(tagId, userId);
        Integer total = memoTagRelationService.countMemos(tagId);
        Criteria criteria = Criteria.of(memoPage, memoLimit, total);
        PageRequest pageRequest = PageRequest.of(
            criteria.getPage(),
            criteria.getLimit(),
            Sort.by(Sort.Direction.DESC, "uTime")
        );
        Page<Memo> result = memoService.getPagedMemos(memoTagRelationService.getMemoIds(tagId), pageRequest, userId);
        Page<MemoTagsResponse> memoTagsPage = result.map(
            memo -> MemoTagsResponse.from(
                memo,
                tagService.getTags(memoTagRelationService.getLinkedTagIds(memo.getId()), userId)
            )
        );
        return PagedMemosTagsResponse.from(tag, memoTagsPage, criteria);
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
        System.out.println(createdMemo.getCreatedAt());
        System.out.println(createdMemo.getCreatedAt());
        System.out.println(createdMemo.getCreatedAt());
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
            List<String> parentTagIds = tagsRelationService.getParentTagsIds(linkedTagId);
            createParentTagsRelations(savedMemo.getId(), parentTagIds);
        }
        for (var addRelation : aiMemoTagsResponse.tagsRelations().added()) {
            tagsRelationService.createRelation(addRelation.parentId(), addRelation.childId());
        }
        for (var deletedRelation : aiMemoTagsResponse.tagsRelations().deleted()) {
            tagsRelationService.deleteRelation(deletedRelation.parentId(), deletedRelation.childId());
        }
        return tags;
    }

    private void createParentTagsRelations(String memoId, List<String> parentTagIds) {
        if (parentTagIds != null && !parentTagIds.isEmpty()) {
            for (var tagId : parentTagIds) {
                memoTagRelationService.createRelation(memoId, tagId, !IS_LINKED_MEMO_TAG);
                createParentTagsRelations(memoId, tagsRelationService.getParentTagsIds(tagId));
            }
        }
    }
}
