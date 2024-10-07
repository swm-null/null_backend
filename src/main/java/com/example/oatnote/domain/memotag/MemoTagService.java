package com.example.oatnote.domain.memotag;

import static com.example.oatnote.domain.memotag.service.client.dto.AICreateStructureResponse.NewTag;
import static com.example.oatnote.domain.memotag.service.client.dto.AICreateStructureResponse.TagsRelations.AddedRelation;
import static com.example.oatnote.domain.memotag.service.client.dto.AICreateStructureResponse.TagsRelations.DeletedRelation;
import static com.example.oatnote.domain.memotag.service.client.dto.innerDto.ProcessedMemoTags.TagsRelations.Relation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.oatnote.event.CreateStructureAsyncEvent;
import com.example.oatnote.domain.memotag.dto.ChildTagsWithMemosResponse;
import com.example.oatnote.domain.memotag.dto.CreateMemoRequest;
import com.example.oatnote.domain.memotag.dto.CreateMemoResponse;
import com.example.oatnote.domain.memotag.dto.CreateMemosRequest;
import com.example.oatnote.domain.memotag.dto.MemosResponse;
import com.example.oatnote.domain.memotag.dto.SearchHistoriesResponse;
import com.example.oatnote.domain.memotag.dto.TagsResponse;
import com.example.oatnote.domain.memotag.dto.SearchMemosRequest;
import com.example.oatnote.domain.memotag.dto.SearchMemosResponse;
import com.example.oatnote.domain.memotag.dto.UpdateMemoRequest;
import com.example.oatnote.domain.memotag.dto.UpdateMemoResponse;
import com.example.oatnote.domain.memotag.dto.UpdateTagRequest;
import com.example.oatnote.domain.memotag.dto.UpdateTagResponse;
import com.example.oatnote.domain.memotag.dto.enums.SortOrderTypeEnum;
import com.example.oatnote.domain.memotag.dto.innerDto.MemoResponse;
import com.example.oatnote.domain.memotag.dto.innerDto.SearchHistoryResponse;
import com.example.oatnote.domain.memotag.dto.innerDto.TagResponse;
import com.example.oatnote.domain.memotag.service.client.AIMemoTagClient;
import com.example.oatnote.domain.memotag.service.client.dto.AICreateEmbeddingResponse;
import com.example.oatnote.domain.memotag.service.client.dto.AICreateMemosResponse;
import com.example.oatnote.domain.memotag.service.client.dto.AICreateStructureRequest;
import com.example.oatnote.domain.memotag.service.client.dto.AICreateStructureResponse;
import com.example.oatnote.domain.memotag.service.client.dto.AICreateTagsRequest;
import com.example.oatnote.domain.memotag.service.client.dto.AICreateTagsResponse;
import com.example.oatnote.domain.memotag.service.client.dto.AISearchMemosRequest;
import com.example.oatnote.domain.memotag.service.client.dto.AISearchMemosResponse;
import com.example.oatnote.domain.memotag.service.client.dto.innerDto.ProcessedMemoTags;
import com.example.oatnote.domain.memotag.service.memo.MemoService;
import com.example.oatnote.domain.memotag.service.memo.model.Memo;
import com.example.oatnote.domain.memotag.service.relation.MemoTagRelationService;
import com.example.oatnote.domain.memotag.service.searchhistory.SearchHistoryService;
import com.example.oatnote.domain.memotag.service.searchhistory.model.SearchHistory;
import com.example.oatnote.domain.memotag.service.tag.TagService;
import com.example.oatnote.domain.memotag.service.tag.edge.model.TagEdge;
import com.example.oatnote.domain.memotag.service.tag.model.Tag;
import com.example.oatnote.web.exception.client.OatIllegalArgumentException;
import com.example.oatnote.web.model.Criteria;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemoTagService {

    private final AIMemoTagClient aiMemoTagClient;
    private final MemoService memoService;
    private final TagService tagService;
    private final MemoTagRelationService memoTagRelationService;
    private final SearchHistoryService searchHistoryService;
    private final ApplicationEventPublisher eventPublisher;

    private final static boolean IS_LINKED_MEMO_TAG = true;
    private final static String TEMP_USER_ID = "70c0d720-fa31-4220-86ff-35163e956bd9"; //todo 삭제

    public CreateMemoResponse createMemoTags(CreateMemoRequest createMemoRequest, String userId) {
        LocalDateTime now = LocalDateTime.now();

        AICreateTagsRequest aiCreateTagsRequest = createMemoRequest.toAICreateMemoRequest(userId);
        AICreateTagsResponse aiCreateTagsResponse = aiMemoTagClient.createTags(aiCreateTagsRequest);

        Memo rawMemo = createMemoRequest.toRawMemo(userId, now);
        eventPublisher.publishEvent(new CreateStructureAsyncEvent(aiCreateTagsResponse, rawMemo, userId, now));

        return CreateMemoResponse.from(rawMemo, aiCreateTagsResponse.tags());
    }

    public void createMemosTags(CreateMemosRequest createMemosRequest) { //todo refactor
        AICreateMemosResponse aiCreateMemosResponse = aiMemoTagClient.createMemosTags(
            createMemosRequest.content(),
            TEMP_USER_ID
        );
        for (var aiMemoTagsResponse : aiCreateMemosResponse.processedMemoTags()) {
            Memo savedMemo = createMemoTags(aiMemoTagsResponse, TEMP_USER_ID);
            updateMemosTagsRelations(aiMemoTagsResponse, savedMemo, TEMP_USER_ID);
        }

        TagEdge tagEdge = new TagEdge(TEMP_USER_ID, aiCreateMemosResponse.newStructure());
        tagService.createTagEdge(tagEdge);
    }

    public MemosResponse getMemos(
        String tagId,
        Integer memoPage,
        Integer memoLimit,
        SortOrderTypeEnum sortOrder,
        String userId
    ) {
        if (Objects.equals(sortOrder, SortOrderTypeEnum.NAME)) {
            throw OatIllegalArgumentException.withDetail("메모는 이름 순으로 정렬할 수 없습니다.");
        }

        tagId = Objects.requireNonNullElse(tagId, userId);

        Integer total = memoTagRelationService.countMemos(tagId, userId);
        Criteria criteria = Criteria.of(memoPage, memoLimit, total);
        PageRequest pageRequest = createPageRequest(criteria.getPage(), criteria.getLimit(), sortOrder);

        Page<Memo> result = memoService.getPagedMemos(
            memoTagRelationService.getMemoIds(tagId, userId),
            pageRequest,
            userId
        );
        Page<MemoResponse> memoTagsPage = result.map(
            memo -> MemoResponse.fromTag(memo, getLinkedTags(memo.getId(), userId))
        );
        return MemosResponse.from(memoTagsPage, criteria);
    }

    public List<TagResponse> getChildTags(String parentTagId, String userId) {
        parentTagId = Objects.requireNonNullElse(parentTagId, userId);

        List<Tag> childTags = tagService.getChildTags(parentTagId, userId);
        return childTags.stream()
            .map(TagResponse::fromTag)
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
        parentTagId = Objects.requireNonNullElse(parentTagId, userId);

        List<String> childTagsIds = tagService.getChildTagsIds(parentTagId);

        Integer total = childTagsIds.size();
        Criteria criteria = Criteria.of(tagPage, tagLimit, total);
        PageRequest pageRequest = createPageRequest(criteria.getPage(), criteria.getLimit(), SortOrderTypeEnum.NAME);

        Page<Tag> result = tagService.getPagedTags(childTagsIds, pageRequest, userId);
        Page<TagsResponse> pagedTags = result.map(
            tag -> new TagsResponse(
                TagResponse.fromTag(tag),
                tagService.getChildTags(tag.getId(), userId).stream()
                    .map(TagResponse::fromTag)
                    .toList()
            )
        );
        Page<MemosResponse> pagedMemos = pagedTags.map(
            pagedTag -> getMemos(pagedTag.tag().id(), memoPage, memoLimit, sortOrder, userId)
        );
        return ChildTagsWithMemosResponse.from(pagedTags, criteria, pagedMemos);
    }

    public SearchHistoriesResponse getSearchHistories(
        String query,
        Integer searchHistoryPage,
        Integer searchHistoryLimit,
        String userId
    ) {
        Integer total = searchHistoryService.countSearchHistories(userId);
        Criteria criteria = Criteria.of(searchHistoryPage, searchHistoryLimit, total);
        PageRequest pageRequest = createPageRequest(criteria.getPage(), criteria.getLimit(), SortOrderTypeEnum.LATEST);
        Page<SearchHistory> result = searchHistoryService.getSearchHistories(query, pageRequest, userId);
        Page<SearchHistoryResponse> pagedSearchHistories = result.map(SearchHistoryResponse::from);
        return SearchHistoriesResponse.from(pagedSearchHistories, criteria);
    }

    public SearchMemosResponse searchMemos(SearchMemosRequest searchMemosRequest, String userId) {
        AISearchMemosRequest aiSearchMemosRequest = searchMemosRequest.toAISearchMemoRequest(userId);
        AISearchMemosResponse aiSearchMemosResponse = aiMemoTagClient.searchMemo(aiSearchMemosRequest);

        List<Memo> memos = switch (aiSearchMemosResponse.type()) {
            case SIMILARITY -> memoService.getMemos(aiSearchMemosResponse.ids(), userId);
            case REGEX -> memoService.getMemosContainingRegex(aiSearchMemosResponse.regex(), userId);
        };

        List<List<Tag>> tagsList = memos.stream()
            .map(memo -> getLinkedTags(memo.getId(), userId))
            .toList();

        SearchMemosResponse searchMemosResponse = SearchMemosResponse.from(
            aiSearchMemosResponse.processedMessage(),
            memos,
            tagsList
        );
        SearchHistory searchHistory = searchMemosRequest.toSearchHistory(searchMemosResponse, userId);
        searchHistoryService.createSearchHistory(searchHistory);
        return searchMemosResponse;
    }

    public UpdateMemoResponse updateMemo(String memoId, UpdateMemoRequest updateMemoRequest, String userId) {
        AICreateEmbeddingResponse aiCreateEmbeddingResponse = aiMemoTagClient.createEmbedding(
            updateMemoRequest.content()
        );
        Memo memo = memoService.getMemo(memoId, userId);
        memo.update(updateMemoRequest.content(), updateMemoRequest.imageUrls(), null,
            aiCreateEmbeddingResponse.embedding());
        Memo updatedMemo = memoService.updateMemo(memo);
        return UpdateMemoResponse.from(updatedMemo, getLinkedTags(memo.getId(), userId));
    }

    public UpdateTagResponse updateTag(String tagId, UpdateTagRequest updateTagRequest, String userId) {
        AICreateEmbeddingResponse aiCreateEmbeddingResponse = aiMemoTagClient.createEmbedding(updateTagRequest.name());
        Tag tag = tagService.getTag(tagId, userId);
        tag.update(updateTagRequest.name(), aiCreateEmbeddingResponse.embedding());
        Tag updatedTag = tagService.updateTag(tag);
        return UpdateTagResponse.from(updatedTag);
    }

    public void deleteMemo(String memoId, String userId) {
        memoTagRelationService.deleteRelationsByMemoId(memoId, userId);
        Memo memo = memoService.getMemo(memoId, userId);
        memoService.deleteMemo(memo);
    }

    public void deleteTag(String tagId, String userId) {
        memoTagRelationService.deleteRelationsByTagId(tagId, userId);
        Tag tag = tagService.getTag(tagId, userId);
        tagService.deleteTag(tag);
    }

    public void deleteUserAllData(String userId) {
        memoTagRelationService.deleteUserAllData(userId);
        memoService.deleteUserAllData(userId);
        tagService.deleteUserAllData(userId);
    }

    @Async
    public void createStructureAsync(
        AICreateTagsResponse aiCreateTagsResponse,
        Memo rawMemo,
        String userId,
        LocalDateTime now
    ) {
        AICreateStructureRequest aiCreateStructureRequest = aiCreateTagsResponse.toAICreateStructureRequest(
            rawMemo,
            userId
        );
        AICreateStructureResponse aiCreateStructureResponse = aiMemoTagClient.createStructure(aiCreateStructureRequest);
        processMemoTag(aiCreateStructureResponse, rawMemo, userId, now);
    }

    void processMemoTag(
        AICreateStructureResponse aiCreateStructureResponse,
        Memo rawMemo,
        String userId,
        LocalDateTime now
    ) {
        for (NewTag newTag : aiCreateStructureResponse.newTags()) {
            Tag tag = newTag.toTag(userId, now);
            tagService.createTag(tag);
        }

        for (AddedRelation addedRelation : aiCreateStructureResponse.tagsRelations().added()) {
            tagService.createRelation(addedRelation.parentId(), addedRelation.childId(), userId);
        }

        for (DeletedRelation deletedRelation : aiCreateStructureResponse.tagsRelations().deleted()) {
            tagService.deleteRelation(deletedRelation.parentId(), deletedRelation.childId(), userId);
        }

        tagService.createTagEdge(new TagEdge(userId, aiCreateStructureResponse.newStructure()));

        Memo processedMemo = aiCreateStructureResponse.processedMemos().get(0).toProcessedMemo(rawMemo);
        memoService.createMemo(processedMemo);
        for (String parentTagId : aiCreateStructureResponse.processedMemos().get(0).parentTagIds()) {
            memoTagRelationService.createRelation(processedMemo.getId(), parentTagId, IS_LINKED_MEMO_TAG, userId);
            List<String> parentTagIds = tagService.getParentTagsIds(parentTagId);
            createParentTagsRelations(processedMemo.getId(), parentTagIds, userId);
        }
    }

    Memo createMemoTags(ProcessedMemoTags aiMemoTagsResponse, String userId) {
        Memo memo = new Memo(
            aiMemoTagsResponse.content(),
            new ArrayList<>(),
            userId,
            "",
            aiMemoTagsResponse.embedding()
        );
        Memo createdMemo = memoService.createMemo(memo);
        for (var newTag : aiMemoTagsResponse.newTags()) {
            Tag tag = new Tag(
                newTag.id(),
                newTag.name(),
                userId,
                newTag.embedding()
            );
            tagService.createTag(tag);
        }
        return createdMemo;
    }

    List<Tag> updateMemosTagsRelations(ProcessedMemoTags processedMemoTags, Memo savedMemo, String userId) {
        for (Relation addRelation : processedMemoTags.tagsRelations().added()) {
            tagService.createRelation(addRelation.parentId(), addRelation.childId(), userId);
        }
        for (Relation deletedRelation : processedMemoTags.tagsRelations().deleted()) {
            tagService.deleteRelation(deletedRelation.parentId(), deletedRelation.childId(), userId);
        }

        List<Tag> tags = new ArrayList<>();
        for (String linkedTagId : processedMemoTags.parentTagIds()) {
            tags.add(tagService.getTag(linkedTagId, userId));
            memoTagRelationService.createRelation(savedMemo.getId(), linkedTagId, IS_LINKED_MEMO_TAG, userId);
            List<String> parentTagIds = tagService.getParentTagsIds(linkedTagId);
            createParentTagsRelations(savedMemo.getId(), parentTagIds, userId);
        }
        return tags;
    }

    void createParentTagsRelations(String memoId, List<String> parentTagIds, String userId) {
        if (Objects.nonNull(parentTagIds) && !parentTagIds.isEmpty()) {
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
            case LATEST -> Sort.by(Sort.Direction.DESC, "uTime");
        };
        return PageRequest.of(page, limit, sort);
    }

    List<Tag> getLinkedTags(String memoId, String userId) {
        List<String> tagIds = memoTagRelationService.getLinkedTagIds(memoId, userId);
        return tagService.getTags(tagIds, userId);
    }
}
