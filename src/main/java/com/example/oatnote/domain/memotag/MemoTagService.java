package com.example.oatnote.domain.memotag;

import static com.example.oatnote.domain.memotag.dto.ChildTagsResponse.ChildTag;
import static com.example.oatnote.domain.memotag.dto.ChildTagsResponse.from;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.oatnote.domain.memotag.dto.ChildTagsResponse;
import com.example.oatnote.domain.memotag.dto.CreateMemoRequest;
import com.example.oatnote.domain.memotag.dto.CreateMemoResponse;
import com.example.oatnote.domain.memotag.dto.CreateMemosRequest;
import com.example.oatnote.domain.memotag.dto.MemosResponse;
import com.example.oatnote.domain.memotag.dto.SearchHistoriesResponse;
import com.example.oatnote.domain.memotag.dto.SearchMemosRequest;
import com.example.oatnote.domain.memotag.dto.SearchMemosResponse;
import com.example.oatnote.domain.memotag.dto.UpdateMemoRequest;
import com.example.oatnote.domain.memotag.dto.UpdateMemoResponse;
import com.example.oatnote.domain.memotag.dto.UpdateTagRequest;
import com.example.oatnote.domain.memotag.dto.UpdateTagResponse;
import com.example.oatnote.domain.memotag.dto.enums.MemoSortOrderTypeEnum;
import com.example.oatnote.domain.memotag.dto.innerDto.MemoResponse;
import com.example.oatnote.domain.memotag.dto.innerDto.SearchHistoryResponse;
import com.example.oatnote.domain.memotag.rabbitmq.MemoTagMessageProducer;
import com.example.oatnote.domain.memotag.service.client.AIMemoTagClient;
import com.example.oatnote.domain.memotag.service.client.dto.AICreateEmbeddingResponse;
import com.example.oatnote.domain.memotag.service.client.dto.AICreateStructureRequest;
import com.example.oatnote.domain.memotag.service.client.dto.AICreateStructureResponse;
import com.example.oatnote.domain.memotag.service.client.dto.AICreateTagsRequest;
import com.example.oatnote.domain.memotag.service.client.dto.AICreateTagsResponse;
import com.example.oatnote.domain.memotag.service.client.dto.AISearchMemosRequest;
import com.example.oatnote.domain.memotag.service.client.dto.AISearchMemosResponse;
import com.example.oatnote.domain.memotag.service.memo.MemoService;
import com.example.oatnote.domain.memotag.service.memo.model.Memo;
import com.example.oatnote.domain.memotag.service.relation.MemoTagRelationService;
import com.example.oatnote.domain.memotag.service.relation.model.MemoTagRelation;
import com.example.oatnote.domain.memotag.service.searchhistory.SearchHistoryService;
import com.example.oatnote.domain.memotag.service.searchhistory.model.SearchHistory;
import com.example.oatnote.domain.memotag.service.tag.TagService;
import com.example.oatnote.domain.memotag.service.tag.edge.model.TagEdge;
import com.example.oatnote.domain.memotag.service.tag.model.Tag;
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
    private final MemoTagMessageProducer memoTagMessageProducer;

    private final static boolean IS_LINKED_MEMO_TAG = true;

    public CreateMemoResponse createMemoTags(CreateMemoRequest createMemoRequest, String userId) {
        LocalDateTime now = LocalDateTime.now();

        AICreateTagsRequest aiCreateTagsRequest = createMemoRequest.toAICreateMemoRequest(userId);
        AICreateTagsResponse aiCreateTagsResponse = aiMemoTagClient.createTags(aiCreateTagsRequest);

        Memo rawMemo = createMemoRequest.toRawMemo(userId, now);

        memoTagMessageProducer.sendCreateStructuresRequest(aiCreateTagsResponse, rawMemo, userId, now);

        return CreateMemoResponse.from(rawMemo, aiCreateTagsResponse.tags());
    }

    public void createMemosTags(CreateMemosRequest createMemosRequest) {
        //todo refactor
    }

    public MemosResponse getMemos(
        String tagId,
        Integer page,
        Integer limit,
        MemoSortOrderTypeEnum sortOrder,
        Boolean isLinked,
        String userId
    ) {
        List<String> memoIds = Objects.isNull(isLinked)
            ? memoTagRelationService.getMemoIds(tagId, userId)
            : memoTagRelationService.getMemoIds(tagId, isLinked, userId);

        Integer total = memoIds.size();
        Criteria criteria = Criteria.of(page, limit, total);
        PageRequest pageRequest = PageRequest.of(criteria.getPage(), criteria.getLimit(), getSort(sortOrder));

        Page<Memo> memos = memoService.getMemos(memoIds, userId, pageRequest);

        // 메모와 링크태그 배치 처리
        List<MemoTagRelation> memoTagRelations = memoTagRelationService.getLinkedMemoTagRelations(memoIds, userId);

        Map<String, List<String>> memoToTagIdsMap = memoTagRelations.stream()
            .collect(Collectors.groupingBy(
                MemoTagRelation::getMemoId,
                Collectors.mapping(MemoTagRelation::getTagId, Collectors.toList())
            ));

        Set<String> tagIds = memoToTagIdsMap.values().stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toSet());

        List<Tag> linkedTags = tagService.getTags(tagIds, userId);

        Map<String, Tag> tagMap = linkedTags.stream()
            .collect(Collectors.toMap(Tag::getId, tag -> tag));

        Map<String, List<Tag>> linkedTagsMap = memoToTagIdsMap.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue().stream()
                    .map(tagMap::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList())
            ));

        Page<MemoResponse> memoResponses = memos.map(memo -> {
            List<Tag> linkedTagsForMemo = linkedTagsMap.getOrDefault(memo.getId(), List.of());
            return MemoResponse.fromTag(memo, linkedTagsForMemo);
        });

        return MemosResponse.from(memoResponses, criteria);
    }

    public ChildTagsResponse getChildTags(
        String tagId,
        Integer page,
        Integer limit,
        String userId
    ) {
        tagId = Objects.requireNonNullElse(tagId, userId);
        Tag tag = tagService.getTag(tagId, userId);

        TagEdge tagEdge = tagService.getTagEdge(userId);
        Map<String, List<String>> tagEdges = tagEdge.getEdges();
        List<String> childTagIds = tagEdges.getOrDefault(tagId, List.of());

        Integer total = childTagIds.size();
        Criteria criteria = Criteria.of(page, limit, total);
        PageRequest pageRequest = PageRequest.of(
            criteria.getPage(),
            criteria.getLimit(),
            Sort.by(Sort.Direction.DESC, "name")
        );
        Page<Tag> childTagsPage = tagService.getTags(childTagIds, userId, pageRequest);

        // 자식의 자식태그들을 배치로 불러옴
        Map<String, List<String>> childToGrandChildTagIdsMap = childTagsPage.stream()
            .collect(Collectors.toMap(
                Tag::getId,
                childTag -> tagEdges.getOrDefault(childTag.getId(), List.of())
            ));

        Set<String> grandChildTagIds = childToGrandChildTagIdsMap.values().stream()
            .flatMap(List::stream)
            .collect(Collectors.toSet());

        Map<String, List<Tag>> grandChildTagsMap = tagService.getTags(grandChildTagIds, userId).stream()
            .collect(Collectors.groupingBy(Tag::getId));

        Page<ChildTag> childTags = childTagsPage.map(childTag -> {
            List<String> grandChildIds = childToGrandChildTagIdsMap.getOrDefault(childTag.getId(), List.of());

            List<Tag> grandChildTags = grandChildIds.stream()
                .map(grandChildTagsMap::get)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .collect(Collectors.toList());
            return ChildTag.from(childTag, grandChildTags);
        });
        return from(tag, childTags, criteria);
    }

    public SearchHistoriesResponse getSearchHistories(
        String query,
        Integer searchHistoryPage,
        Integer searchHistoryLimit,
        String userId
    ) {
        Integer total = searchHistoryService.countSearchHistories(userId);
        Criteria criteria = Criteria.of(searchHistoryPage, searchHistoryLimit, total);
        PageRequest pageRequest = PageRequest.of(
            criteria.getPage(),
            criteria.getLimit(),
            Sort.by(Sort.Direction.DESC, "cTime")
        );
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
        memo.update( //todo metadata
            updateMemoRequest.content(),
            updateMemoRequest.imageUrls(),
            aiCreateEmbeddingResponse.embedding()
        );
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

    // rabbitmq 수신
    public void createStructures(
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
        tagService.processTags(aiCreateStructureResponse, userId, now);

        Memo processedMemo = aiCreateStructureResponse.processedMemos().get(0).toProcessedMemo(rawMemo);
        memoService.createMemo(processedMemo);
        for (String parentTagId : aiCreateStructureResponse.processedMemos().get(0).parentTagIds()) {
            memoTagRelationService.createRelation(processedMemo.getId(), parentTagId, IS_LINKED_MEMO_TAG, userId);
            List<String> parentTagIds = tagService.getParentTagsIds(parentTagId, userId);
            createParentTagsRelations(processedMemo.getId(), parentTagIds, userId);
        }
    }

    void createParentTagsRelations(String memoId, List<String> parentTagIds, String userId) {
        if (Objects.nonNull(parentTagIds) && !parentTagIds.isEmpty()) {
            for (var tagId : parentTagIds) {
                memoTagRelationService.createRelation(memoId, tagId, !IS_LINKED_MEMO_TAG, userId);
                createParentTagsRelations(memoId, tagService.getParentTagsIds(tagId, userId), userId);
            }
        }
    }

    List<Tag> getLinkedTags(String memoId, String userId) {
        List<String> tagIds = memoTagRelationService.getLinkedTagIds(memoId, userId);
        return tagService.getTags(tagIds, userId);
    }

    Sort getSort(MemoSortOrderTypeEnum sortOrder) {
        return switch (sortOrder) {
            case LATEST -> Sort.by(Sort.Direction.DESC, "uTime");
            case OLDEST -> Sort.by(Sort.Direction.ASC, "uTime");
        };
    }
}
