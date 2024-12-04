package com.example.oatnote.web.validation;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;

import com.example.oatnote.memo.service.producer.SseMessageProducer;
import com.example.oatnote.web.validation.enums.ActionType;
import com.example.oatnote.web.validation.helper.AuthenticationContextHelper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class ProcessingMemoCountAspect {

    private final RedissonClient redissonClient;
    private final SseMessageProducer sseMessageProducer;
    private static final String PROCESSING_MEMOS_COUNT_KEY_PREFIX = "processingMemoCountKey:";

    @Before("@annotation(annotation)")
    public void handleIncrement(ProcessingMemoCount annotation) {
        if (annotation.action() == ActionType.INCREMENT) {
            String userId = AuthenticationContextHelper.getUserId();
            RAtomicLong memoCounter = redissonClient.getAtomicLong(PROCESSING_MEMOS_COUNT_KEY_PREFIX + userId);
            memoCounter.incrementAndGet();
            memoCounter.expire(Instant.now().plusSeconds(600));
            sseMessageProducer.publishProcessingMemosCount(userId);
        }
    }

    @After("@annotation(annotation)")
    public void handleDecrementOrJustPublish(ProcessingMemoCount annotation) {
        String userId = AuthenticationContextHelper.getUserId();
        if (annotation.action() == ActionType.DECREMENT) {
            RAtomicLong memoCounter = redissonClient.getAtomicLong(PROCESSING_MEMOS_COUNT_KEY_PREFIX + userId);
            memoCounter.decrementAndGet();
            memoCounter.expire(Instant.now().plusSeconds(600));
        }
        if (annotation.action() == ActionType.JUST_PUBLISH || annotation.action() == ActionType.DECREMENT) {
            sseMessageProducer.publishProcessingMemosCount(userId);
        }
    }

    @AfterThrowing("@annotation(annotation)")
    public void handleException(ProcessingMemoCount annotation) {
        String userId = AuthenticationContextHelper.getUserId();
        if (annotation.action() == ActionType.INCREMENT) {
            RAtomicLong memoCounter = redissonClient.getAtomicLong(PROCESSING_MEMOS_COUNT_KEY_PREFIX + userId);
            memoCounter.decrementAndGet();
            log.warn("Exception occurred. Decremented processing memo count for userId: {}", userId);
        }
        sseMessageProducer.publishProcessingMemosCount(userId);
    }
}
