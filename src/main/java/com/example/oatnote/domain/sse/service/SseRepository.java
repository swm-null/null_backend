package com.example.oatnote.domain.sse.service;

import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class SseRepository {

    private final RedissonClient redissonClient;

    @Value("${sse.redis.key-prefix}")
    private String keyPrefix;

    public SseRepository(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public void save(String userId, SseEmitter emitter) {
        RMap<String, List<SseEmitter>> emitters = redissonClient.getMap(keyPrefix);
        emitters.computeIfAbsent(userId, key -> new CopyOnWriteArrayList<>()).add(emitter);
    }

    public List<SseEmitter> findByUserId(String userId) {
        RMap<String, List<SseEmitter>> emitters = redissonClient.getMap(keyPrefix);
        return emitters.getOrDefault(userId, List.of());
    }

    public void delete(String userId, SseEmitter emitter) {
        RMap<String, List<SseEmitter>> emitters = redissonClient.getMap(keyPrefix);
        List<SseEmitter> userEmitters = emitters.get(userId);
        if (userEmitters != null) {
            userEmitters.remove(emitter);
            if (userEmitters.isEmpty()) {
                emitters.remove(userId);
            }
        }
    }
}
