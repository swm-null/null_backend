package com.example.oatnote.domain.sse.service;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Repository
public class SseRepository {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public void save(String userId, SseEmitter emitter) {
        emitters.put(userId, emitter);
    }

    public SseEmitter findById(String userId) {
        return emitters.get(userId);
    }

    public void deleteById(String userId) {
        emitters.remove(userId);
    }
}
