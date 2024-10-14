package com.example.oatnote.domain.memotag.rabbitmq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.example.oatnote.domain.memotag.MemoTagService;
import com.example.oatnote.domain.memotag.rabbitmq.dto.MemoTagMessage;
import com.example.oatnote.web.exception.server.OatExternalServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemoTagMessageConsumer {

    private final MemoTagService memoTagService;
    private final ObjectMapper objectMapper;

    @RabbitListener(queues = "oatnote_user_queue", ackMode = "MANUAL")
    public void receiveMemoTagRequest(Message message, Channel channel) {
        try {
            MemoTagMessage memoTagMessage = parseMessageBody(message.getBody());
            log.info("rabbitMQ structures request 수신. userId: {}", memoTagMessage.userId());

            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false); //todo refactor

            memoTagService.createStructures(
                memoTagMessage.aiCreateTagsResponse(),
                memoTagMessage.rawMemo(),
                memoTagMessage.userId(),
                memoTagMessage.time()
            );
        } catch (OatExternalServiceException e) {
            log.error("Error rabbitMQ consuming message: {}", e.getDetail());
        } catch (Exception e) {
            log.error("Error rabbitMQ consuming message: {}", e.getMessage());
        }
    }

    private MemoTagMessage parseMessageBody(byte[] body) {
        try {
            return objectMapper.readValue(body, MemoTagMessage.class);
        } catch (Exception e) {
            log.error("Error rabbitMQ parsing message body");
            throw new OatExternalServiceException("Error rabbitMQ parsing message body");
        }
    }
}
