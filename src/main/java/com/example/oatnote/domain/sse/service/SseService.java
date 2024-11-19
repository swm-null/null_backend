package com.example.oatnote.domain.sse.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SseService {

    private final SseRepository sseRepository;

    @Value("${sse.timeout}")
    private Long defaultTimeout;

    public SseEmitter subscribe(String userId) {
        SseEmitter emitter = new SseEmitter(defaultTimeout);
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
