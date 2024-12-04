package com.example.oatnote.sse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.example.oatnote.sse.service.SseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SseController implements SseApiDoc {

    private final SseService sseService;

    @GetMapping("/sse/subscribe")
    public ResponseEntity<SseEmitter> subscribe(
        @AuthenticationPrincipal String userId
    ) {
        SseEmitter emitter = sseService.subscribe(userId);
        return ResponseEntity.ok(emitter);
    }
}
