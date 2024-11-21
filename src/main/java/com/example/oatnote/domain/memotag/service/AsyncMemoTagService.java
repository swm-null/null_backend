package com.example.oatnote.domain.memotag.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RAtomicLong;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.oatnote.domain.memotag.service.client.AiMemoTagClient;
import com.example.oatnote.domain.memotag.service.client.dto.AiCreateStructureRequest;
import com.example.oatnote.domain.memotag.service.client.dto.AiCreateStructureResponse;
import com.example.oatnote.domain.memotag.service.client.dto.innerDto.RawTag;
import com.example.oatnote.domain.memotag.service.memo.MemoService;
import com.example.oatnote.domain.memotag.service.memo.model.Memo;
import com.example.oatnote.domain.memotag.service.producer.SseMessageProducer;
import com.example.oatnote.domain.memotag.service.relation.MemoTagRelationService;
import com.example.oatnote.domain.memotag.service.relation.model.MemoTagRelation;
import com.example.oatnote.domain.memotag.service.tag.TagService;
import com.example.oatnote.web.validation.ProcessingMemoCount;
import com.example.oatnote.web.validation.enums.ActionType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AsyncMemoTagService {

    private final MemoService memoService;
    private final TagService tagService;
    private final MemoTagRelationService memoTagRelationService;
    private final AiMemoTagClient aiMemoTagClient;
    private final RedissonClient redissonClient;

    private static final boolean IS_LINKED_MEMO_TAG = true;
    private static final String LOCK_KEY_PREFIX = "memoTagLock:";

    @Async("AsyncMemoTagExecutor")
    @ProcessingMemoCount(action = ActionType.DECREMENT)
    public void createStructure(List<RawTag> rawTags, Memo rawMemo, String userId, LocalDateTime time) {
        RLock lock = redissonClient.getLock(LOCK_KEY_PREFIX + userId);
        lock.lock(20, TimeUnit.MINUTES);
        try {
            AiCreateStructureRequest aiCreateStructureRequest
                = AiCreateStructureRequest.from(rawTags, rawMemo, userId, time);
            AiCreateStructureResponse aiCreateStructureResponse
                = aiMemoTagClient.createStructure(aiCreateStructureRequest);

            memoTagRelationService.deleteRelationsByMemoId(rawMemo.getId(), userId);

            processMemoTag(aiCreateStructureResponse, rawMemo.getId(), userId);
        } finally {
            lock.unlock();
        }
    }

    @Async("AsyncMemoTagExecutor")
    @ProcessingMemoCount(action = ActionType.DECREMENT)
    public void createStructure(String fileUrl, String userId) {
        RLock lock = redissonClient.getLock(LOCK_KEY_PREFIX + userId);
        lock.lock(20, TimeUnit.MINUTES);
        try {
            AiCreateStructureResponse aiCreateStructureResponse = aiMemoTagClient.createStructure(fileUrl, userId);
            processMemoTag(aiCreateStructureResponse, null, userId);
        } finally {
            lock.unlock();
        }
    }

    void processMemoTag(AiCreateStructureResponse aiCreateStructureResponse, String memoId, String userId) {
        List<Memo> memos = new ArrayList<>();
        List<MemoTagRelation> memoTagRelations = new ArrayList<>();

        Map<String, List<String>> reversedTagEdge = aiCreateStructureResponse.newReversedStructure();
        Set<String> visitedTagIds = new HashSet<>();

        for (AiCreateStructureResponse.ProcessedMemo processedMemo : aiCreateStructureResponse.processedMemos()) {
            Memo memo = new Memo(
                memoId,
                processedMemo.content(),
                processedMemo.imageUrls(),
                processedMemo.voiceUrls(),
                userId,
                processedMemo.metadata(),
                processedMemo.embedding(),
                processedMemo.embeddingMetadata(),
                processedMemo.timestamp()
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
        tagService.processTags(aiCreateStructureResponse, userId);
        memoService.createMemos(memos, userId);
        memoTagRelationService.createRelations(memoTagRelations, userId);
    }
}
