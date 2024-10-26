package com.example.oatnote.domain.memotag.rabbitmq;

import java.util.List;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.example.oatnote._commons.message.DeleteFilesMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilesMessageProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.queues.file.exchange}")
    private String exchangeName;

    @Value("${spring.rabbitmq.queues.file.routing-key}")
    private String routingKey;

    @Value("${spring.rabbitmq.queues.dlx.exchange}")
    private String dlxExchangeName;

    @Value("${spring.rabbitmq.queues.dlx.routing-key}")
    private String dlxRoutingKey;

    @Retryable(
        retryFor = {Exception.class},
        maxAttempts = 2,
        backoff = @Backoff(delay = 1000)
    )
    public void sendDeleteFilesRequest(List<String> fileUrls, String userId) {
        log.info("Producing RabbitMQ delete files request. userId: {}", userId);
        DeleteFilesMessage deleteFilesMessage = DeleteFilesMessage.of(fileUrls, userId);
        rabbitTemplate.convertAndSend(exchangeName, routingKey, deleteFilesMessage);
    }

    @Recover
    public void sendToDLX(Exception e, List<String> fileUrls, String userId) {
        log.warn("Failed to send message. Sending to DLX. userId: {}, fileUrls: {}", userId, fileUrls);
        log.error("Exception: {}", e.getMessage(), e);
        DeleteFilesMessage deleteFilesMessage = DeleteFilesMessage.of(fileUrls, userId);
        rabbitTemplate.convertAndSend(dlxExchangeName, dlxRoutingKey, deleteFilesMessage);
    }
}
