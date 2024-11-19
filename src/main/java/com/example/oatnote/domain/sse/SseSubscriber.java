package com.example.oatnote.domain.sse;

import org.springframework.stereotype.Component;

import com.example.oatnote.domain.sse.service.SseService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SseSubscriber {

    private final SseService sseService;

    public void onMessage(String message) {
        String[] parts = message.split(":");
        String userId = parts[0];
        int payload = Integer.parseInt(parts[1]);

        sseService.sendToUser(userId, payload);
    }
}
