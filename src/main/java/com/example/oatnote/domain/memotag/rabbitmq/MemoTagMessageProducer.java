package com.example.oatnote.domain.memotag.rabbitmq;

import java.time.LocalDateTime;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.example.oatnote._commons.message.MemoTagMessage;
import com.example.oatnote.domain.memotag.service.client.dto.AiCreateTagsResponse;
import com.example.oatnote.domain.memotag.service.memo.model.Memo;
import com.example.oatnote.web.exception.server.OatExternalServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemoTagMessageProducer {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.rabbitmq.queues.user.exchange}")
    private String exchange;

    public void sendCreateStructuresRequest(
        AiCreateTagsResponse aiCreateTagsResponse,
        Memo rawMemo,
        String userId,
        LocalDateTime time
    ) {
        log.info("RabbitMQ 구조 요청 송신. userId: {}", userId);
        MemoTagMessage message = new MemoTagMessage(aiCreateTagsResponse, rawMemo, userId, time);

        try {
            MessageProperties props = new MessageProperties();
            Message rabbitMessage = MessageBuilder
                .withBody(objectMapper.writeValueAsBytes(message))
                .andProperties(props)
                .build();

            String dynamicRoutingKey = String.format("user.%s", userId);
            rabbitTemplate.send(exchange, dynamicRoutingKey, rabbitMessage);
        } catch (JsonProcessingException e) {
            throw new OatExternalServiceException("Error parsing message body. userId: " + userId);
        }
    }
}
