package com.example.oatnote.domain.sse.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.example.oatnote._commons.message.SendProcessingMemosCountMessage;
import com.example.oatnote.domain.sse.service.SseService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SseMessageConsumer {

    private final SseService sseService;

    @RabbitListener(queues = "${spring.rabbitmq.queues.sse.send-processing-memos-count.queue}")
    public void receiveProcessingMemosCountMessage(@Payload SendProcessingMemosCountMessage message) {
        log.info("Received SSE message from RabbitMQ. userId: {}", message.userId());

        try {
            sseService.sendProcessingMemosCountToUser(message.userId());
            log.info("Successfully sent SSE message. userId: {}", message.userId());
        } catch (Exception e) {
            log.error("Error sending SSE message. userId: {}", message.userId(), e);
        }
    }
}
