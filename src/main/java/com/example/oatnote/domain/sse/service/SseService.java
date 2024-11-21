package com.example.oatnote.domain.sse.service;

import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SseService {

    private final SseRepository sseRepository;
    private final RedissonClient redissonClient;

    private static final String PROCESSING_MEMOS_COUNT_KEY_PREFIX = "processingMemoCount:";

    public SseEmitter subscribe(String userId) {
        SseEmitter emitter = new SseEmitter(3600000L);
        sseRepository.save(userId, emitter);

        sendProcessingMemosCountToUser(userId);

        emitter.onCompletion(() -> sseRepository.delete(userId, emitter));
        emitter.onTimeout(() -> sseRepository.delete(userId, emitter));

        return emitter;
    }

    public void sendProcessingMemosCountToUser(String userId) {
        RAtomicLong memoCounter = redissonClient.getAtomicLong(PROCESSING_MEMOS_COUNT_KEY_PREFIX + userId);
        int memoProcessingCount = (int) memoCounter.get();

        List<SseEmitter> emitters = sseRepository.findByUserId(userId);
        if (emitters.isEmpty()) {
            log.info("No active emitters for userId: {}", userId);
            return;
        }

        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().data(memoProcessingCount));
            } catch (Exception e) {
                sseRepository.delete(userId, emitter);
                log.error("Error sending message to userId: {}, removing emitter", userId, e);
            }
        }
    }
}
