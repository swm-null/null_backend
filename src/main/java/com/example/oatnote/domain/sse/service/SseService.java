package com.example.oatnote.domain.sse.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SseService {

    private final SseRepository sseRepository;

    public SseEmitter subscribe(String userId) {
        SseEmitter emitter = new SseEmitter(60L * 1000 * 60); // 1시간 타임아웃
        sseRepository.save(userId, emitter);

        emitter.onCompletion(() -> sseRepository.deleteById(userId));
        emitter.onTimeout(() -> sseRepository.deleteById(userId));

        return emitter;
    }

    public void sendToUser(String userId, int message) {
        SseEmitter emitter = sseRepository.findById(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().data(message));
            } catch (Exception e) {
                sseRepository.deleteById(userId);
            }
        }
    }
}
