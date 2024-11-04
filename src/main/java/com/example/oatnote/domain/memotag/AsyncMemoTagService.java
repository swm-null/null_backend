package com.example.oatnote.domain.memotag;

import java.time.LocalDateTime;
import java.util.*;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.oatnote.domain.memotag.service.client.AiMemoTagClient;
import com.example.oatnote.domain.memotag.service.client.dto.AiCreateStructureRequest;
import com.example.oatnote.domain.memotag.service.client.dto.AiCreateStructureResponse;
import com.example.oatnote.domain.memotag.service.client.dto.AiCreateTagsResponse;
import com.example.oatnote.domain.memotag.service.memo.MemoService;
import com.example.oatnote.domain.memotag.service.memo.model.Memo;
import com.example.oatnote.domain.memotag.service.relation.MemoTagRelationService;
import com.example.oatnote.domain.memotag.service.relation.model.MemoTagRelation;
import com.example.oatnote.domain.memotag.service.tag.TagService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AsyncMemoTagService {

    private final MemoService memoService;
    private final TagService tagService;
    private final MemoTagRelationService memoTagRelationService;
    private final AiMemoTagClient aiMemoTagClient;
    private final RedissonClient redissonClient;

    private static final boolean IS_LINKED_MEMO_TAG = true;
    private static final String LOCK_KEY_PREFIX = "memoTagLock:";

    @Async("AsyncMemoTagExecutor")
    public void createStructures(
        AiCreateTagsResponse aiCreateTagsResponse,
        Memo rawMemo,
        String userId,
        LocalDateTime now
    ) {
        RLock lock = redissonClient.getLock(LOCK_KEY_PREFIX + userId);
        lock.lock();

        try {
            AiCreateStructureRequest aiCreateStructureRequest = aiCreateTagsResponse.toAiCreateStructureRequest(
                rawMemo,
                userId
            );
            AiCreateStructureResponse aiCreateStructureResponse = aiMemoTagClient.createStructure(aiCreateStructureRequest);
            processMemoTag(aiCreateStructureResponse, rawMemo, userId, now);
        } finally {
            lock.unlock();
        }
    }

    void processMemoTag(
        AiCreateStructureResponse aiCreateStructureResponse,
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

        for (AiCreateStructureResponse.ProcessedMemo processedMemo : aiCreateStructureResponse.processedMemos()) {
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
}
