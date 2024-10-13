package com.example.oatnote.domain.memotag.rabbitmq;

import java.io.IOException;

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
    public void receiveMemoTagRequest(Message message, Channel channel) throws IOException {
        log.info("rabbitMQ structures request 수신. userId: cec09524-1ddc-453f-8383-af228977e587");

        MemoTagMessage memoTagMessage = parseMessageBody(message.getBody());
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        memoTagService.createStructures(
            memoTagMessage.aiCreateTagsResponse(),
            memoTagMessage.rawMemo(),
            memoTagMessage.userId(),
            memoTagMessage.time()
        );
    }

    private MemoTagMessage parseMessageBody(byte[] body) {
        try {
            return objectMapper.readValue(body, MemoTagMessage.class);
        } catch (Exception e) {
            throw OatExternalServiceException.withDetail("Error rabbitMQ parsing message body");
        }
    }
}
