package com.example.oatnote.domain.memotag;

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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
import com.example.oatnote.domain.memotag.rabbitmq.FilesMessageProducer;
import com.example.oatnote.domain.memotag.rabbitmq.MemoTagMessageProducer;
import com.example.oatnote.domain.memotag.service.client.AIMemoTagClient;
import com.example.oatnote.domain.memotag.service.client.dto.AICreateEmbeddingResponse;
import com.example.oatnote.domain.memotag.service.client.dto.AICreateMetadataResponse;
import com.example.oatnote.domain.memotag.service.client.dto.AICreateStructureRequest;
import com.example.oatnote.domain.memotag.service.client.dto.AICreateStructureResponse;
import com.example.oatnote.domain.memotag.service.client.dto.AICreateTagsRequest;
import com.example.oatnote.domain.memotag.service.client.dto.AICreateTagsResponse;
import com.example.oatnote.domain.memotag.service.client.dto.AISearchMemosUsingAiResponse;
import com.example.oatnote.domain.memotag.service.client.dto.AISearchMemosUsingDbResponse;
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
    private final FilesMessageProducer filesMessageProducer;

    private final static boolean IS_LINKED_MEMO_TAG = true;

    public CreateMemoResponse createMemo(CreateMemoRequest createMemoRequest, String userId) {
        LocalDateTime now = LocalDateTime.now();

        AICreateTagsRequest aiCreateTagsRequest = createMemoRequest.toAICreateMemoRequest(userId);
        AICreateTagsResponse aiCreateTagsResponse = aiMemoTagClient.createTags(aiCreateTagsRequest);

        Memo rawMemo = createMemoRequest.toRawMemo(userId, now);

        memoTagMessageProducer.sendCreateStructuresRequest(aiCreateTagsResponse, rawMemo, userId, now);

        return CreateMemoResponse.from(rawMemo, aiCreateTagsResponse.tags());
    }

    public void createMemos(CreateMemosRequest createMemosRequest) {
        //todo refactor
    }

    public CreateSearchHistoryResponse createSearchHistory(
        CreateSearchHistoryRequest createSearchHistoryRequest,
        String userId
    ) {
        SearchHistory searchHistory = createSearchHistoryRequest.toSearchHistory(userId);
        SearchHistory createdSearchHistory = searchHistoryService.createSearchHistory(searchHistory);
        return CreateSearchHistoryResponse.from(createdSearchHistory);
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

        Map<String, List<Tag>> grandChildTagsMap = tagService.getTags(
                new ArrayList<>(grandChildTagIds),
                userId
            )
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

        Page<Memo> memos = memoService.getMemos(memoIds, userId, pageRequest);
        List<String> pagedMemoIds = memos.stream()
            .map(Memo::getId)
            .toList();
        Map<String, List<Tag>> linkedTagsMap = getLinkedTagsMap(pagedMemoIds, userId);

        Page<MemoResponse> memoResponses = memos.map(memo -> {
            List<Tag> linkedTagsForMemo = linkedTagsMap.getOrDefault(memo.getId(), List.of());
            return MemoResponse.fromTag(memo, linkedTagsForMemo);
        });

        return MemosResponse.from(memoResponses, criteria);
    }

    public SearchMemosUsingAiResponse searchMemosUsingAi(String searchHistoryId, String userId) {
        String query = searchHistoryService.getQuery(searchHistoryId, userId);

        AISearchMemosUsingAiResponse aiSearchMemosUsingAiResponse = aiMemoTagClient.searchMemoUsingAi(query, userId);

        List<Memo> memos = switch (aiSearchMemosUsingAiResponse.type()) {
            case SIMILARITY -> memoService.getMemos(aiSearchMemosUsingAiResponse.memoIds(), userId);
            case REGEX -> memoService.getMemosContainingRegex(aiSearchMemosUsingAiResponse.regex(), userId);
        };

        List<String> memoIds = memos.stream()
            .map(Memo::getId)
            .toList();
        Map<String, List<Tag>> linkedTagsMap = getLinkedTagsMap(memoIds, userId);
        List<MemoResponse> memoResponses = memos.stream()
            .map(memo -> {
                List<Tag> linkedTagsForMemo = linkedTagsMap.getOrDefault(memo.getId(), List.of());
                return MemoResponse.fromTag(memo, linkedTagsForMemo);
            })
            .toList();

        SearchMemosUsingAiResponse searchMemosUsingAiResponse = SearchMemosUsingAiResponse.from(
            aiSearchMemosUsingAiResponse.processedMessage(),
            memoResponses
        );
        searchHistoryService.updateAiResponse(searchHistoryId, searchMemosUsingAiResponse, userId);

        return searchMemosUsingAiResponse;
    }

    public SearchMemosUsingDbResponse searchMemosUsingDb(String searchHistoryId, String userId) {
        String query = searchHistoryService.getQuery(searchHistoryId, userId);

        AISearchMemosUsingDbResponse aiSearchMemosUsingDbResponse = aiMemoTagClient.searchMemoUsingDb(query, userId);

        List<Memo> memos = memoService.getMemos(aiSearchMemosUsingDbResponse.memoIds(), userId);
        List<String> memoIds = memos.stream()
            .map(Memo::getId)
            .toList();
        Map<String, List<Tag>> linkedTagsMap = getLinkedTagsMap(memoIds, userId);
        List<MemoResponse> memoResponses = memos.stream()
            .map(memo -> {
                List<Tag> linkedTagsForMemo = linkedTagsMap.getOrDefault(memo.getId(), List.of());
                return MemoResponse.fromTag(memo, linkedTagsForMemo);
            })
            .toList();

        SearchMemosUsingDbResponse searchMemosUsingDbResponse = SearchMemosUsingDbResponse.from(memoResponses);
        searchHistoryService.updateDbResponse(searchHistoryId, searchMemosUsingDbResponse, userId);

        return searchMemosUsingDbResponse;
    }

    public UpdateMemoResponse updateMemo(String memoId, UpdateMemoRequest updateMemoRequest, String userId) {
        String updatedContent = updateMemoRequest.content();
        List<String> updatedImageUrls = updateMemoRequest.imageUrls();

        Memo memo = memoService.getMemo(memoId, userId);

        AICreateEmbeddingResponse aiCreateEmbeddingResponse = null;

        boolean isContentChanged = !updatedContent.equals(memo.getContent());
        if (isContentChanged) {
            aiCreateEmbeddingResponse = aiMemoTagClient.createEmbedding(updatedContent);
        }

        AICreateMetadataResponse aiCreateMetadataResponse = aiMemoTagClient.createMetadata(
            updatedContent,
            updatedImageUrls
        );

        List<Double> embedding = Objects.nonNull(aiCreateEmbeddingResponse)
            ? aiCreateEmbeddingResponse.embedding() : memo.getEmbedding();
        String metadata = Objects.nonNull(aiCreateMetadataResponse)
            ? aiCreateMetadataResponse.metadata() : memo.getMetadata();
        List<Double> embeddingMetadata = Objects.nonNull(aiCreateMetadataResponse)
            ? aiCreateMetadataResponse.embeddingMetadata() : memo.getEmbeddingMetadata();

        memo.update(
            updatedContent,
            updatedImageUrls,
            embedding,
            metadata,
            embeddingMetadata
        );

        Memo updatedMemo = memoService.updateMemo(memo);

        // 삭제된 이미지 전송
        List<String> deletedImageUrls = memo.getImageUrls().stream()
            .filter(imageUrl -> !updatedImageUrls.contains(imageUrl))
            .collect(Collectors.toList());
        if (!deletedImageUrls.isEmpty()) {
            filesMessageProducer.sendDeleteFilesRequest(deletedImageUrls, userId);
        }

        Map<String, List<Tag>> linkedTagsMap = getLinkedTagsMap(List.of(memo.getId()), userId);
        List<Tag> linkedTags = linkedTagsMap.getOrDefault(memo.getId(), List.of());
        return UpdateMemoResponse.from(updatedMemo, linkedTags);
    }

    public UpdateTagResponse updateTag(String tagId, UpdateTagRequest updateTagRequest, String userId) {
        AICreateEmbeddingResponse aiCreateEmbeddingResponse = aiMemoTagClient.createEmbedding(updateTagRequest.name());
        Tag tag = tagService.getTag(tagId, userId);
        tag.update(updateTagRequest.name(), aiCreateEmbeddingResponse.embedding());
        Tag updatedTag = tagService.updateTag(tag);
        return UpdateTagResponse.from(updatedTag);
    }

    public UpdateMemoTagsResponse updateMemoTags(
        String memoId,
        UpdateMemoTagsRequest updateMemoTagsRequest,
        String userId
    ) {
        LocalDateTime now = LocalDateTime.now();

        AICreateTagsRequest aiCreateTagsRequest = updateMemoTagsRequest.toAICreateMemoRequest(userId);
        AICreateTagsResponse aiCreateTagsResponse = aiMemoTagClient.createTags(aiCreateTagsRequest);

        Memo rawMemo = memoService.getMemo(memoId, userId);

        processDeletedFiles(rawMemo.getImageUrls(), updateMemoTagsRequest.imageUrls(), userId);

        rawMemo.update(
            updateMemoTagsRequest.content(),
            updateMemoTagsRequest.imageUrls()
        );

        memoTagMessageProducer.sendCreateStructuresRequest(aiCreateTagsResponse, rawMemo, userId, now);

        return UpdateMemoTagsResponse.from(rawMemo, aiCreateTagsResponse.tags());
    }

    public void deleteMemo(String memoId, String userId) {
        List<String> fileUrls = memoService.getFileUrls(List.of(memoId), userId);
        filesMessageProducer.sendDeleteFilesRequest(fileUrls, userId);

        memoTagRelationService.deleteRelationsByMemoId(memoId, userId);
        memoService.deleteMemo(memoId, userId);
    }

    public void deleteTag(String tagId, String userId) {
        List<String> memoIds = memoTagRelationService.getMemoIds(tagId, userId);

        List<String> fileUrls = memoService.getFileUrls(memoIds, userId);
        filesMessageProducer.sendDeleteFilesRequest(fileUrls, userId);

        memoService.deleteMemos(memoIds, userId);
        memoTagRelationService.deleteRelationsByTagId(tagId, userId);

        TagEdge tagEdge = tagService.getTagEdge(userId);
        Map<String, List<String>> tagEdges = tagEdge.getEdges();
        Map<String, List<String>> reverseTagEdges = tagEdge.getReversedEdges();

        Queue<String> queue = new LinkedList<>();
        queue.add(tagId);
        Set<String> visitedTagIds = new HashSet<>();

        while (!queue.isEmpty()) {
            String currentTagId = queue.poll();
            tagEdges.remove(currentTagId);

            if (visitedTagIds.add(currentTagId)) {
                List<String> childTagIds = tagEdges.getOrDefault(currentTagId, List.of());
                queue.addAll(childTagIds);

                for (String childTagId : childTagIds) {
                    reverseTagEdges.getOrDefault(childTagId, new ArrayList<>()).remove(currentTagId);
                }
            }
        }
        tagEdge.updateEdges(tagEdges, reverseTagEdges);
        tagService.updateTagEdge(tagEdge, userId);
        tagService.deleteTags(visitedTagIds, userId);
    }

    public void deleteUserAllData(String userId) {
        filesMessageProducer.sendDeleteAllFilesRequest(userId);
        memoTagRelationService.deleteUserAllData(userId);
        memoService.deleteUserAllData(userId);
        tagService.deleteUserAllData(userId);
        searchHistoryService.deleteUserAllData(userId);
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

    public void createDefaultTagStructureForNewUser(String rootTagName, String userId) {
        tagService.createDefaultTagStructureForNewUser(rootTagName, userId);
    }

    void processMemoTag(
        AICreateStructureResponse aiCreateStructureResponse,
        Memo rawMemo,
        String userId,
        LocalDateTime now
    ) {
        if (Objects.nonNull(rawMemo.getId())) {
            memoTagRelationService.deleteRelationsByMemoId(rawMemo.getId(), userId);
        }

        List<Memo> memos = new ArrayList<>();
        List<MemoTagRelation> memoTagRelations = new ArrayList<>();

        Map<String, List<String>> reversedTagEdge = aiCreateStructureResponse.newReversedStructure();
        Set<String> visitedTagIds = new HashSet<>();

        for (AICreateStructureResponse.ProcessedMemo processedMemo : aiCreateStructureResponse.processedMemos()) {
            Memo memo = rawMemo.process(
                processedMemo.metadata(),
                processedMemo.embedding(),
                processedMemo.embeddingMetadata()
            );
            memos.add(memo);

            for (String linkedTagId : processedMemo.parentTagIds()) {
                MemoTagRelation memoTagRelation = MemoTagRelation.of(
                    memo.getId(),
                    linkedTagId,
                    IS_LINKED_MEMO_TAG,
                    userId
                );
                memoTagRelations.add(memoTagRelation);
            }

            Queue<String> queue = new LinkedList<>(processedMemo.parentTagIds());
            while (!queue.isEmpty()) {
                String currentTagId = queue.poll();
                if (visitedTagIds.add(currentTagId)) {
                    List<String> parentTagIds = reversedTagEdge.getOrDefault(currentTagId, List.of());
                    queue.addAll(parentTagIds);
                    for (String parentTagId : parentTagIds) {
                        MemoTagRelation memoTagRelation = MemoTagRelation.of(
                            memo.getId(),
                            parentTagId,
                            !IS_LINKED_MEMO_TAG,
                            userId
                        );
                        memoTagRelations.add(memoTagRelation);
                    }
                }
            }
        }
        tagService.processTags(aiCreateStructureResponse, userId, now);
        memoService.createMemos(memos, userId);
        memoTagRelationService.createRelations(memoTagRelations, userId);
    }

    Map<String, List<Tag>> getLinkedTagsMap(List<String> memoIds, String userId) {
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
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList())
            ));
    }

    void processDeletedFiles(List<String> originalImageUrls, List<String> updatedImageUrls, String userId) {
        List<String> deletedImageUrls = originalImageUrls.stream()
            .filter(imageUrl -> !updatedImageUrls.contains(imageUrl))
            .collect(Collectors.toList());

        if (!deletedImageUrls.isEmpty()) {
            filesMessageProducer.sendDeleteFilesRequest(deletedImageUrls, userId);
        }
    }
}
