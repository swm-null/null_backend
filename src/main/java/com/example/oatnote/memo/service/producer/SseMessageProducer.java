package com.example.oatnote.memo.service.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.oatnote._commons.message.SendProcessingMemosCountMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SseMessageProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.queues.sse.exchange}")
    private String sseExchange;

    @Value("${spring.rabbitmq.queues.sse.send-processing-memos-count.routing-key}")
    private String sendProcessingMemosCountRoutingKey;

    public void publishProcessingMemosCount(String userId) {
        SendProcessingMemosCountMessage sendProcessingMemosCountMessage = SendProcessingMemosCountMessage.of(userId);
        log.info("Publishing SSE message to RabbitMQ. userId: {}", userId);
        rabbitTemplate.convertAndSend(sseExchange, sendProcessingMemosCountRoutingKey, sendProcessingMemosCountMessage);
    }
}
