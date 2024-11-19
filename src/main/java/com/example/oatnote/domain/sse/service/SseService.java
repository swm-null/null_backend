package com.example.oatnote.domain.sse.service;

import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SseService {

    private final SseRepository sseRepository;
    private final RedissonClient redissonClient;

    @Value("${sse.timeout}")
    private Long defaultTimeout;

    private static final String MEMO_PROCESSING_COUNT_KEY_PREFIX = "processingMemoCount:";

    public SseEmitter subscribe(String userId) {
        SseEmitter emitter = new SseEmitter(defaultTimeout);
        sseRepository.save(userId, emitter);

        RAtomicLong memoCounter = redissonClient.getAtomicLong(MEMO_PROCESSING_COUNT_KEY_PREFIX + userId);
        int memoProcessingCount = (int) memoCounter.get();
        sendMemoProcessingCountToUser(userId, memoProcessingCount);

        emitter.onCompletion(() -> sseRepository.deleteById(userId));
        emitter.onTimeout(() -> sseRepository.deleteById(userId));

        return emitter;
    }

    public void sendMemoProcessingCountToUser(String userId, int memoProcessingCount) {
        SseEmitter emitter = sseRepository.findById(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().data(memoProcessingCount));
            } catch (Exception e) {
                sseRepository.deleteById(userId);
            }
        }
    }
}
