package com.example.oatnote.domain.memotag.service.publisher;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemoProcessingPublisher {

    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${sse.topic}")
    private String topic;

    public void publish(String userId, int memoProcessingCount) {
        redisTemplate.convertAndSend(topic, userId + ":" + memoProcessingCount);
    }
}
