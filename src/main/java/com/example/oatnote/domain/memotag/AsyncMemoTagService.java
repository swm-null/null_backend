package com.example.oatnote.domain.memotag;

import java.util.*;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.oatnote.domain.memotag.service.aiClient.AiMemoTagClient;
import com.example.oatnote.domain.memotag.service.aiClient.dto.AiCreateStructureRequest;
import com.example.oatnote.domain.memotag.service.aiClient.dto.AiCreateStructureResponse;
import com.example.oatnote.domain.memotag.service.aiClient.dto.AiCreateTagsResponse;
import com.example.oatnote.domain.memotag.service.memo.MemoService;
import com.example.oatnote.domain.memotag.service.memo.model.Memo;
import com.example.oatnote.domain.memotag.service.relation.MemoTagRelationService;
import com.example.oatnote.domain.memotag.service.relation.model.MemoTagRelation;
import com.example.oatnote.domain.memotag.service.tag.TagService;

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
    public void createStructure(AiCreateTagsResponse aiCreateTagsResponse, Memo memo, String userId) {
        RLock lock = redissonClient.getLock(LOCK_KEY_PREFIX + userId);
        lock.lock();
        try {
            AiCreateStructureRequest aiCreateStructureRequest
                = aiCreateTagsResponse.toAiCreateStructureRequest(memo, userId);
            AiCreateStructureResponse aiCreateStructureResponse
                = aiMemoTagClient.createStructure(aiCreateStructureRequest);

            memoTagRelationService.deleteRelationsByMemoId(memo.getId(), userId);

            processMemoTag(aiCreateStructureResponse, memo.getId(), userId);
        } finally {
            lock.unlock();
        }
    }

    @Async("AsyncMemoTagExecutor")
    public void createStructure(String fileUrl, String userId) {
        RLock lock = redissonClient.getLock(LOCK_KEY_PREFIX + userId);
        lock.lock();
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
            Memo memo = Memo.of(
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
