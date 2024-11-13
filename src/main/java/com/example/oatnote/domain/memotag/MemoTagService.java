package com.example.oatnote.domain.memotag;

import static com.example.oatnote.domain.memotag.dto.TagsResponse.ChildTag;
import static com.example.oatnote.domain.memotag.dto.TagsResponse.from;

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
import com.example.oatnote.domain.memotag.service.aiClient.AiMemoTagClient;
import com.example.oatnote.domain.memotag.service.aiClient.dto.AiCreateEmbeddingResponse;
import com.example.oatnote.domain.memotag.service.aiClient.dto.AiCreateMetadataResponse;
import com.example.oatnote.domain.memotag.service.aiClient.dto.AiCreateTagsRequest;
import com.example.oatnote.domain.memotag.service.aiClient.dto.AiCreateTagsResponse;
import com.example.oatnote.domain.memotag.service.aiClient.dto.AiSearchMemosUsingAiResponse;
import com.example.oatnote.domain.memotag.service.aiClient.dto.AiSearchMemosUsingDbResponse;
import com.example.oatnote.domain.memotag.service.memo.MemoService;
import com.example.oatnote.domain.memotag.service.memo.model.Memo;
import com.example.oatnote.domain.memotag.service.relation.MemoTagRelationService;
import com.example.oatnote.domain.memotag.service.relation.model.MemoTagRelation;
import com.example.oatnote.domain.memotag.service.searchhistory.SearchHistoryService;
import com.example.oatnote.domain.memotag.service.searchhistory.model.SearchHistory;
import com.example.oatnote.domain.memotag.service.tag.TagService;
import com.example.oatnote.domain.memotag.service.tag.edge.model.TagEdge;
import com.example.oatnote.domain.memotag.service.tag.model.Tag;
import com.example.oatnote.domain.user.service.UserService;
import com.example.oatnote.web.model.Criteria;

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
    private final FilesMessageProducer filesMessageProducer;

    public CreateMemoResponse createMemo(CreateMemoRequest createMemoRequest, String userId) {
        AiCreateTagsRequest aiCreateTagsRequest = createMemoRequest.toAiCreateMemoRequest(userId);
        AiCreateTagsResponse aiCreateTagsResponse = aiMemoTagClient.createTags(aiCreateTagsRequest);

        Memo memo = createMemoRequest.toRawMemo(userId);

        asyncMemoTagService.createStructure(aiCreateTagsResponse, memo, userId);

        return CreateMemoResponse.from(memo, aiCreateTagsResponse.tags());
    }

    public void createMemos(CreateMemosRequest createMemosRequest, String userId) {
        userId = Objects.requireNonNullElse(userId, userService.getUserIdByEmail(createMemosRequest.email()));
        asyncMemoTagService.createStructure(createMemosRequest.fileUrl(), userId);
    }

    public CreateSearchHistoryResponse createSearchHistory(
        CreateSearchHistoryRequest createSearchHistoryRequest,
        String userId
    ) {
        SearchHistory searchHistory = createSearchHistoryRequest.toSearchHistory(userId);
        SearchHistory createdSearchHistory = searchHistoryService.createSearchHistory(searchHistory);
        return CreateSearchHistoryResponse.from(createdSearchHistory);
    }

    public List<TagResponse> getParentTags(String tagId, String userId) {

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

    public SearchHistoriesResponse getSearchHistories(
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
        Page<SearchHistory> result = searchHistoryService.getSearchHistories(pageRequest, userId);
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
        Page<MemoResponse> memoResponses = getMemoResponses(memos, userId);
        return MemosResponse.from(memoResponses, criteria);
    }

    public SearchMemosUsingAiResponse searchMemosUsingAi(String searchHistoryId, String userId) {
        String query = searchHistoryService.getQuery(searchHistoryId, userId);

        AiSearchMemosUsingAiResponse aiSearchMemosUsingAiResponse = aiMemoTagClient.searchMemoUsingAi(query, userId);

        List<Memo> memos = switch (aiSearchMemosUsingAiResponse.type()) {
            case SIMILARITY -> memoService.getMemos(aiSearchMemosUsingAiResponse.memoIds(), userId);
            case REGEX -> memoService.getMemosContainingRegex(aiSearchMemosUsingAiResponse.regex(), userId);
        };

        List<MemoResponse> memoResponses = getMemoResponses(memos, userId);

        SearchMemosUsingAiResponse searchMemosUsingAiResponse = SearchMemosUsingAiResponse.from(
            aiSearchMemosUsingAiResponse.processedMessage(),
            memoResponses
        );
        searchHistoryService.updateAiResponse(searchHistoryId, searchMemosUsingAiResponse, userId);

        return searchMemosUsingAiResponse;
    }

    public SearchMemosUsingDbResponse searchMemosUsingDb(String searchHistoryId, String userId) {
        String query = searchHistoryService.getQuery(searchHistoryId, userId);

        AiSearchMemosUsingDbResponse aiSearchMemosUsingDbResponse = aiMemoTagClient.searchMemoUsingDb(query, userId);

        List<Memo> memos = memoService.getMemos(aiSearchMemosUsingDbResponse.memoIds(), userId);
        List<MemoResponse> memoResponses = getMemoResponses(memos, userId);

        SearchMemosUsingDbResponse searchMemosUsingDbResponse = SearchMemosUsingDbResponse.from(memoResponses);
        searchHistoryService.updateDbResponse(searchHistoryId, searchMemosUsingDbResponse, userId);

        return searchMemosUsingDbResponse;
    }

    public UpdateMemoResponse updateMemo(String memoId, UpdateMemoRequest updateMemoRequest, String userId) {
        String updatedContent = updateMemoRequest.content();
        List<String> updatedImageUrls = updateMemoRequest.imageUrls();
        List<String> updatedVoiceUrls = updateMemoRequest.voiceUrls();

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
        MemoResponse memoResponse = getMemoResponses(List.of(updatedMemo), userId).get(0);
        return UpdateMemoResponse.from(memoResponse);
    }

    public UpdateTagResponse updateTag(String tagId, UpdateTagRequest updateTagRequest, String userId) {
        AiCreateEmbeddingResponse aiCreateEmbeddingResponse = aiMemoTagClient.createEmbedding(updateTagRequest.name());
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
        AiCreateTagsRequest aiCreateTagsRequest = updateMemoTagsRequest.toAiCreateMemoRequest(userId);
        AiCreateTagsResponse aiCreateTagsResponse = aiMemoTagClient.createTags(aiCreateTagsRequest);

        Memo memo = memoService.getMemo(memoId, userId);

        processDeletedFiles(memo, updateMemoTagsRequest.imageUrls(), updateMemoTagsRequest.voiceUrls(), userId);

        memo.update(
            updateMemoTagsRequest.content(),
            updateMemoTagsRequest.imageUrls(),
            updateMemoTagsRequest.voiceUrls()
        );

        asyncMemoTagService.createStructure(aiCreateTagsResponse, memo, userId);

        return UpdateMemoTagsResponse.from(memo, aiCreateTagsResponse.tags());
    }

    public void deleteMemo(String memoId, String userId) {
        List<String> fileUrls = memoService.getFileUrls(List.of(memoId), userId);
        sendDeleteFilesRequest(fileUrls, userId);
        memoTagRelationService.deleteRelationsByMemoId(memoId, userId);
        memoService.deleteMemo(memoId, userId);
    }

    public void deleteTag(String tagId, String userId) {
        List<String> memoIds = memoTagRelationService.getMemoIds(tagId, userId);
        memoService.deleteMemos(memoIds, userId);

        List<String> fileUrls = memoService.getFileUrls(memoIds, userId);
        filesMessageProducer.sendDeleteFilesRequest(fileUrls, userId);

        memoTagRelationService.deleteRelationsByTagId(tagId, userId);

        deleteTagAndDescendants(tagId, userId);
    }

    public void deleteUserAllData(String userId) {
        filesMessageProducer.sendDeleteAllFilesRequest(userId);
        memoTagRelationService.deleteUserAllData(userId);
        memoService.deleteUserAllData(userId);
        tagService.deleteUserAllData(userId);
        searchHistoryService.deleteUserAllData(userId);
    }

    public void createDefaultTagStructureForNewUser(String rootTagName, String userId) {
        tagService.createDefaultTagStructureForNewUser(rootTagName, userId);
    }

    List<MemoResponse> getMemoResponses(List<Memo> memos, String userId) {
        Map<String, List<Tag>> linkedTagsMap = getLinkedTagsMap(memos, userId);
        return memos.stream()
            .map(memo -> {
                List<Tag> linkedTagsForMemo = linkedTagsMap.getOrDefault(memo.getId(), List.of());
                return MemoResponse.fromTag(memo, linkedTagsForMemo);
            })
            .toList();
    }

    Page<MemoResponse> getMemoResponses(Page<Memo> memos, String userId) {
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

    private void updateTagEdge(
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
            filesMessageProducer.sendDeleteFilesRequest(fileUrls, userId);
        }
    }
}
