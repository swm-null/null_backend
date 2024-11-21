package com.example.oatnote.domain.memotag.service;

import static com.example.oatnote.domain.memotag.dto.TagsResponse.ChildTag;
import static com.example.oatnote.domain.memotag.dto.TagsResponse.from;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.oatnote.domain.memotag.dto.CreateChildTagRequest;
import com.example.oatnote.domain.memotag.dto.CreateChildTagResponse;
import com.example.oatnote.domain.memotag.dto.CreateMemoRequest;
import com.example.oatnote.domain.memotag.dto.CreateMemoResponse;
import com.example.oatnote.domain.memotag.dto.CreateMemosRequest;
import com.example.oatnote.domain.memotag.dto.CreateSearchHistoryRequest;
import com.example.oatnote.domain.memotag.dto.CreateSearchHistoryResponse;
import com.example.oatnote.domain.memotag.dto.MemosResponse;
import com.example.oatnote.domain.memotag.dto.SearchHistoriesResponse;
import com.example.oatnote.domain.memotag.dto.SearchMemosUsingAiResponse;
import com.example.oatnote.domain.memotag.dto.SearchMemosUsingDbResponse;
import com.example.oatnote.domain.memotag.dto.TagsResponse;
import com.example.oatnote.domain.memotag.dto.UpdateMemoRequest;
import com.example.oatnote.domain.memotag.dto.UpdateMemoResponse;
import com.example.oatnote.domain.memotag.dto.UpdateMemoTagsRequest;
import com.example.oatnote.domain.memotag.dto.UpdateMemoTagsResponse;
import com.example.oatnote.domain.memotag.dto.UpdateTagRequest;
import com.example.oatnote.domain.memotag.dto.UpdateTagResponse;
import com.example.oatnote.domain.memotag.dto.enums.MemoSortOrderTypeEnum;
import com.example.oatnote.domain.memotag.dto.innerDto.MemoResponse;
import com.example.oatnote.domain.memotag.dto.innerDto.SearchHistoryResponse;
import com.example.oatnote.domain.memotag.dto.innerDto.TagResponse;
import com.example.oatnote.domain.memotag.service.client.AiMemoTagClient;
import com.example.oatnote.domain.memotag.service.client.dto.AiCreateEmbeddingResponse;
import com.example.oatnote.domain.memotag.service.client.dto.AiCreateMetadataResponse;
import com.example.oatnote.domain.memotag.service.client.dto.AiCreateTagsRequest;
import com.example.oatnote.domain.memotag.service.client.dto.AiCreateTagsResponse;
import com.example.oatnote.domain.memotag.service.client.dto.AiSearchMemosUsingAiResponse;
import com.example.oatnote.domain.memotag.service.client.dto.AiSearchMemosUsingDbResponse;
import com.example.oatnote.domain.memotag.service.client.dto.innerDto.RawTag;
import com.example.oatnote.domain.memotag.service.memo.MemoService;
import com.example.oatnote.domain.memotag.service.memo.model.Memo;
import com.example.oatnote.domain.memotag.service.producer.FileMessageProducer;
import com.example.oatnote.domain.memotag.service.relation.MemoTagRelationService;
import com.example.oatnote.domain.memotag.service.relation.model.MemoTagRelation;
import com.example.oatnote.domain.memotag.service.searchhistory.SearchHistoryService;
import com.example.oatnote.domain.memotag.service.searchhistory.model.SearchHistory;
import com.example.oatnote.domain.memotag.service.tag.TagService;
import com.example.oatnote.domain.memotag.service.tag.edge.model.TagEdge;
import com.example.oatnote.domain.memotag.service.tag.model.Tag;
import com.example.oatnote.domain.user.service.UserService;
import com.example.oatnote.web.model.Criteria;
import com.example.oatnote.web.validation.ProcessingMemoCount;
import com.example.oatnote.web.validation.enums.ActionType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemoTagService {

    private final MemoService memoService;
    private final TagService tagService;
    private final MemoTagRelationService memoTagRelationService;
    private final SearchHistoryService searchHistoryService;
    private final AsyncMemoTagService asyncMemoTagService;
    private final AiMemoTagClient aiMemoTagClient;
    private final UserService userService;

    private final FileMessageProducer fileMessageProducer;

    @ProcessingMemoCount(action = ActionType.INCREMENT)
    public CreateMemoResponse createMemo(CreateMemoRequest request, String userId) {
        LocalDateTime now = LocalDateTime.now(); //todo refactor
        AiCreateTagsRequest aiCreateTagsRequest = request.toAiCreateMemoRequest(userId);
        AiCreateTagsResponse aiCreateTagsResponse = aiMemoTagClient.createTags(aiCreateTagsRequest);

        Memo rawMemo = request.toRawMemo(userId, aiCreateTagsResponse.metadata());
        List<RawTag> rawTags = aiCreateTagsResponse.tags();
        asyncMemoTagService.createStructure(rawTags, rawMemo, userId, now);

        return CreateMemoResponse.from(rawMemo, aiCreateTagsResponse.tags());
    }

    @ProcessingMemoCount(action = ActionType.INCREMENT)
    public CreateMemoResponse createLinkedMemo(String tagId, CreateMemoRequest request, String userId) {
        tagId = Objects.requireNonNullElse(tagId, userId);
        Tag tag = tagService.getTag(tagId, userId);

        LocalDateTime now = LocalDateTime.now();
        AiCreateMetadataResponse aiCreateMetadataResponse = aiMemoTagClient.createMetadata(
            request.content(),
            request.imageUrls(),
            request.voiceUrls()
        );
        Memo rawMemo = request.toRawMemo(userId, aiCreateMetadataResponse.metadata());
        RawTag rawTag = new RawTag(tag.getId(), tag.getName(), false);
        asyncMemoTagService.createStructure(List.of(rawTag), rawMemo, userId, now);

        return CreateMemoResponse.from(rawMemo, List.of(rawTag));
    }

    @ProcessingMemoCount(action = ActionType.INCREMENT)
    public void createMemos(CreateMemosRequest request, String userId) {
        userId = Objects.requireNonNullElse(userId, userService.getUserIdByEmail(request.email()));
        asyncMemoTagService.createStructure(request.fileUrl(), userId);
    }

    @ProcessingMemoCount(action = ActionType.JUST_PUBLISH)
    public CreateChildTagResponse createChildTag(String tagId, CreateChildTagRequest request, String userId) {
        tagService.validateTagExist(request.name(), userId);
        tagId = Objects.requireNonNullElse(tagId, userId);
        AiCreateEmbeddingResponse aiCreateEmbeddingResponse = aiMemoTagClient.createEmbedding(request.name());
        Tag childTag = request.toTag(userId, aiCreateEmbeddingResponse.embedding());
        Tag createdChildTag = tagService.createChildTag(tagId, childTag, userId);
        return CreateChildTagResponse.from(createdChildTag);
    }

    public CreateSearchHistoryResponse createSearchHistory(
        CreateSearchHistoryRequest request,
        String userId
    ) {
        SearchHistory searchHistory = request.toSearchHistory(userId);
        SearchHistory createdSearchHistory = searchHistoryService.createSearchHistory(searchHistory);
        return CreateSearchHistoryResponse.from(createdSearchHistory);
    }

    public List<TagResponse> getAncestorTags(String tagId, String userId) {
        List<Tag> parentTags = tagService.getAncestorTags(tagId, userId);
        return parentTags.stream()
            .map(TagResponse::fromTag)
            .toList();
    }

    public List<TagResponse> getChildTags(String tagId, String userId) {
        tagId = Objects.requireNonNullElse(tagId, userId);
        List<Tag> childTags = tagService.getChildTags(tagId, userId);
        return childTags.stream()
            .map(TagResponse::fromTag)
            .toList();
    }

    public TagsResponse getTags(String tagId, Integer page, Integer limit, String userId) {
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
            Sort.by(Sort.Direction.ASC, "name")
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

        Map<String, List<Tag>> grandChildTagsMap = tagService.getTags(new ArrayList<>(grandChildTagIds), userId)
            .stream()
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

    public SearchHistoriesResponse getSearchHistories(Integer page, Integer limit, String userId) {
        Integer total = searchHistoryService.countSearchHistories(userId);
        Criteria criteria = Criteria.of(page, limit, total);
        PageRequest pageRequest = PageRequest.of(
            criteria.getPage(),
            criteria.getLimit(),
            Sort.by(Sort.Direction.DESC, "cTime")
        );

        Page<SearchHistory> result = searchHistoryService.getSearchHistories(pageRequest, userId);

        List<String> allMemoIds = result.stream()
            .flatMap(searchHistory -> Stream.concat(
                searchHistory.getAiMemoIds().stream(),
                searchHistory.getDbMemoIds().stream()
            ))
            .distinct()
            .toList();

        Map<String, MemoResponse> memoResponseMap = getMemoResponses(allMemoIds, userId).stream()
            .collect(Collectors.toMap(MemoResponse::id, memo -> memo));

        Page<SearchHistoryResponse> pagedSearchHistories = result.map(searchHistory -> {
            List<MemoResponse> aiMemoResponses = searchHistory.getAiMemoIds().stream()
                .map(memoResponseMap::get)
                .filter(Objects::nonNull)
                .toList();
            SearchMemosUsingAiResponse aiResponse = SearchMemosUsingAiResponse.from(
                searchHistory.getAiDescription(),
                aiMemoResponses
            );

            List<MemoResponse> dbMemoResponses = searchHistory.getDbMemoIds().stream()
                .map(memoResponseMap::get)
                .filter(Objects::nonNull)
                .toList();
            SearchMemosUsingDbResponse dbResponse = SearchMemosUsingDbResponse.from(dbMemoResponses);

            return SearchHistoryResponse.from(searchHistory, aiResponse, dbResponse);
        });

        return SearchHistoriesResponse.from(pagedSearchHistories, criteria);
    }

    public MemosResponse getMemos(
        String tagId,
        Integer page,
        Integer limit,
        MemoSortOrderTypeEnum sortOrder,
        Boolean isLinked,
        String userId
    ) {
        tagId = Objects.requireNonNullElse(tagId, userId);

        List<String> memoIds = Objects.isNull(isLinked)
            ? memoTagRelationService.getMemoIds(tagId, userId)
            : memoTagRelationService.getMemoIds(tagId, isLinked, userId);

        Integer total = memoIds.size();
        Criteria criteria = Criteria.of(page, limit, total);
        Sort sort = switch (sortOrder) {
            case LATEST -> Sort.by(Sort.Direction.DESC, "uTime");
            case OLDEST -> Sort.by(Sort.Direction.ASC, "uTime");
        };
        PageRequest pageRequest = PageRequest.of(criteria.getPage(), criteria.getLimit(), sort);

        Page<MemoResponse> memoResponses = getMemoResponses(memoIds, userId, pageRequest);
        return MemosResponse.from(memoResponses, criteria);
    }

    public SearchMemosUsingAiResponse searchMemosUsingAi(String searchHistoryId, String userId) {
        String query = searchHistoryService.getQuery(searchHistoryId, userId);
        AiSearchMemosUsingAiResponse aiSearchMemosUsingAiResponse = aiMemoTagClient.searchMemoUsingAi(query, userId);

        String processedMessage = aiSearchMemosUsingAiResponse.processedMessage();
        List<String> memoIds = aiSearchMemosUsingAiResponse.memoIds();

        searchHistoryService.updateAiResponse(searchHistoryId, processedMessage, memoIds, userId);

        List<MemoResponse> memoResponses = getMemoResponses(memoIds, userId);
        return SearchMemosUsingAiResponse.from(processedMessage, memoResponses);
    }

    public SearchMemosUsingDbResponse searchMemosUsingDb(String searchHistoryId, String userId) {
        String query = searchHistoryService.getQuery(searchHistoryId, userId);
        AiSearchMemosUsingDbResponse aiSearchMemosUsingDbResponse = aiMemoTagClient.searchMemoUsingDb(query, userId);
        List<String> memoIds = aiSearchMemosUsingDbResponse.memoIds();

        searchHistoryService.updateDbResponse(searchHistoryId, memoIds, userId);

        List<MemoResponse> memoResponses = getMemoResponses(memoIds, userId);
        return SearchMemosUsingDbResponse.from(memoResponses);
    }

    @ProcessingMemoCount(action = ActionType.JUST_PUBLISH)
    public UpdateMemoResponse updateMemo(String memoId, UpdateMemoRequest request, String userId) {
        String updatedContent = request.content();
        List<String> updatedImageUrls = request.imageUrls();
        List<String> updatedVoiceUrls = request.voiceUrls();

        Memo memo = memoService.getMemo(memoId, userId);

        AiCreateEmbeddingResponse aiCreateEmbeddingResponse = null;

        boolean isContentChanged = !updatedContent.equals(memo.getContent());
        if (isContentChanged) {
            aiCreateEmbeddingResponse = aiMemoTagClient.createEmbedding(updatedContent);
        }

        AiCreateMetadataResponse aiCreateMetadataResponse = aiMemoTagClient.createMetadata(
            updatedContent,
            updatedImageUrls,
            updatedVoiceUrls
        );
        List<Double> embedding = Objects.nonNull(aiCreateEmbeddingResponse)
            ? aiCreateEmbeddingResponse.embedding() : memo.getEmbedding();
        String metadata = Objects.nonNull(aiCreateMetadataResponse)
            ? aiCreateMetadataResponse.metadata() : memo.getMetadata();
        List<Double> embeddingMetadata = Objects.nonNull(aiCreateMetadataResponse)
            ? aiCreateMetadataResponse.embeddingMetadata() : memo.getEmbeddingMetadata();

        processDeletedFiles(memo, updatedImageUrls, updatedVoiceUrls, userId);

        memo.update(
            updatedContent,
            updatedImageUrls,
            updatedVoiceUrls,
            metadata,
            embedding,
            embeddingMetadata
        );
        Memo updatedMemo = memoService.updateMemo(memo);
        MemoResponse memoResponse = getMemoResponses(List.of(updatedMemo.getId()), userId).get(0);
        return UpdateMemoResponse.from(memoResponse);
    }

    @ProcessingMemoCount(action = ActionType.JUST_PUBLISH)
    public UpdateTagResponse updateTag(String tagId, UpdateTagRequest request, String userId) {
        AiCreateEmbeddingResponse aiCreateEmbeddingResponse = aiMemoTagClient.createEmbedding(request.name());
        Tag tag = tagService.getTag(tagId, userId);
        tag.update(request.name(), aiCreateEmbeddingResponse.embedding());
        Tag updatedTag = tagService.updateTag(tag);
        return UpdateTagResponse.from(updatedTag);
    }

    @ProcessingMemoCount(action = ActionType.INCREMENT)
    public UpdateMemoTagsResponse updateMemoTags(
        String memoId,
        UpdateMemoTagsRequest request,
        String userId
    ) {
        LocalDateTime now = LocalDateTime.now();
        AiCreateTagsRequest aiCreateTagsRequest = request.toAiCreateMemoRequest(userId);
        AiCreateTagsResponse aiCreateTagsResponse = aiMemoTagClient.createTags(aiCreateTagsRequest);

        Memo memo = memoService.getMemo(memoId, userId);
        processDeletedFiles(memo, request.imageUrls(), request.voiceUrls(), userId);
        memo.update(
            request.content(),
            request.imageUrls(),
            request.voiceUrls(),
            aiCreateTagsResponse.metadata()
        );
        List<RawTag> rawTags = aiCreateTagsResponse.tags();

        asyncMemoTagService.createStructure(rawTags, memo, userId, now);

        return UpdateMemoTagsResponse.from(memo, aiCreateTagsResponse.tags());
    }

    @ProcessingMemoCount(action = ActionType.JUST_PUBLISH)
    public void deleteMemo(String memoId, String userId) {
        List<String> fileUrls = memoService.getFileUrls(List.of(memoId), userId);
        sendDeleteFilesRequest(fileUrls, userId);
        memoTagRelationService.deleteRelationsByMemoId(memoId, userId);
        memoService.deleteMemo(memoId, userId);
    }

    @ProcessingMemoCount(action = ActionType.JUST_PUBLISH)
    public void deleteTag(String tagId, String userId) {
        List<String> memoIds = memoTagRelationService.getMemoIds(tagId, userId);
        List<String> fileUrls = memoService.getFileUrls(memoIds, userId);

        memoService.deleteMemos(memoIds, userId);
        fileMessageProducer.publishDeleteFiles(fileUrls, userId);
        memoTagRelationService.deleteRelationsByTagId(tagId, userId);
        deleteTagAndDescendants(tagId, userId);
    }

    public void deleteUserAllData(String userId) {
        fileMessageProducer.handleFailureToDLX(userId);
        memoTagRelationService.deleteUserAllData(userId);
        memoService.deleteUserAllData(userId);
        tagService.deleteUserAllData(userId);
        searchHistoryService.deleteUserAllData(userId);
    }

    public void createDefaultTagStructureForNewUser(String rootTagName, String userId) {
        tagService.createDefaultTagStructureForNewUser(rootTagName, userId);
    }

    List<MemoResponse> getMemoResponses(List<String> memoIds, String userId) {
        List<Memo> memos = memoService.getMemos(memoIds, userId);
        Map<String, List<Tag>> linkedTagsMap = getLinkedTagsMap(memos, userId);
        return memos.stream()
            .map(memo -> {
                List<Tag> linkedTagsForMemo = linkedTagsMap.getOrDefault(memo.getId(), List.of());
                return MemoResponse.fromTag(memo, linkedTagsForMemo);
            })
            .toList();
    }

    Page<MemoResponse> getMemoResponses(List<String> memoIds, String userId, PageRequest pageRequest) {
        Page<Memo> memos = memoService.getMemos(memoIds, userId, pageRequest);
        Map<String, List<Tag>> linkedTagsMap = getLinkedTagsMap(memos.getContent(), userId);
        return memos.map(memo -> {
            List<Tag> linkedTagsForMemo = linkedTagsMap.getOrDefault(memo.getId(), List.of());
            return MemoResponse.fromTag(memo, linkedTagsForMemo);
        });
    }

    Map<String, List<Tag>> getLinkedTagsMap(List<Memo> memos, String userId) {
        List<String> memoIds = memos.stream()
            .map(Memo::getId)
            .toList();
        List<MemoTagRelation> memoTagRelations = memoTagRelationService.getLinkedMemoTagRelations(memoIds, userId);

        Map<String, List<String>> memoToTagIdsMap = memoTagRelations.stream()
            .collect(Collectors.groupingBy(
                MemoTagRelation::getMemoId,
                Collectors.mapping(MemoTagRelation::getTagId, Collectors.toList())
            ));

        Set<String> tagIds = memoToTagIdsMap.values().stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toSet());

        List<Tag> linkedTags = tagService.getTags(new ArrayList<>(tagIds), userId);

        Map<String, Tag> tagMap = linkedTags.stream()
            .collect(Collectors.toMap(Tag::getId, tag -> tag));

        return memoToTagIdsMap.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue().stream()
                    .map(tagMap::get)
                    .toList()
            ));
    }

    void deleteTagAndDescendants(String tagId, String userId) {
        TagEdge tagEdge = tagService.getTagEdge(userId);
        Map<String, List<String>> tagEdges = tagEdge.getEdges();
        Map<String, List<String>> reverseTagEdges = tagEdge.getReversedEdges();

        Queue<String> queue = new LinkedList<>();
        queue.add(tagId);
        Set<String> visitedTagIds = new HashSet<>();

        while (!queue.isEmpty()) {
            String currentTagId = queue.poll();

            if (!visitedTagIds.contains(currentTagId)) {
                visitedTagIds.add(currentTagId);

                updateTagEdge(currentTagId, tagEdges, reverseTagEdges, userId);

                List<String> childTagIds = tagEdges.getOrDefault(currentTagId, List.of());
                queue.addAll(childTagIds);
                tagEdges.remove(currentTagId);
            }
        }
        tagEdge.updateEdges(tagEdges, reverseTagEdges);
        tagService.updateTagEdge(tagEdge, userId);

        tagService.deleteTags(visitedTagIds, userId);
    }

    void updateTagEdge(
        String tagId,
        Map<String, List<String>> tagEdges,
        Map<String, List<String>> reverseTagEdges,
        String userId
    ) {
        List<String> parentTagIds = reverseTagEdges.getOrDefault(tagId, new ArrayList<>());
        for (String parentTagId : parentTagIds) {
            List<String> childTagIds = tagEdges.getOrDefault(parentTagId, new ArrayList<>());
            childTagIds.remove(tagId);
            if (childTagIds.isEmpty() && !Objects.equals(parentTagId, userId)) {
                tagEdges.remove(parentTagId);
            }
        }
        reverseTagEdges.remove(tagId);
    }

    void processDeletedFiles(Memo memo, List<String> updatedImageUrls, List<String> updatedVoiceUrls, String userId) {
        List<String> deletedFilesUrls = Stream.concat(
            memo.getImageUrls().stream().filter(imageUrl -> !updatedImageUrls.contains(imageUrl)),
            memo.getVoiceUrls().stream().filter(voiceUrl -> !updatedVoiceUrls.contains(voiceUrl))
        ).toList();

        sendDeleteFilesRequest(deletedFilesUrls, userId);
    }

    void sendDeleteFilesRequest(List<String> fileUrls, String userId) {
        if (!fileUrls.isEmpty()) {
            fileMessageProducer.publishDeleteFiles(fileUrls, userId);
        }
    }
}
