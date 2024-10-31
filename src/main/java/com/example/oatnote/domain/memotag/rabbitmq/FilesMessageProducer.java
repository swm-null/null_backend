package com.example.oatnote.domain.memotag.rabbitmq;

import java.util.List;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.example.oatnote._commons.message.DeleteUserAllFilesMessage;
import com.example.oatnote._commons.message.DeleteFilesMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilesMessageProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.queues.file.exchange}")
    private String fileExchangeName;

    @Value("${spring.rabbitmq.queues.file.delete.routing-key}")
    private String deleteFileRoutingKey;

    @Value("${spring.rabbitmq.queues.file.delete-user-all.routing-key}")
    private String deleteUserAllFilesRoutingKey;

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
        rabbitTemplate.convertAndSend(fileExchangeName, deleteFileRoutingKey, deleteFilesMessage);
    }

    @Retryable(
        retryFor = {Exception.class},
        maxAttempts = 2,
        backoff = @Backoff(delay = 1000)
    )
    public void sendDeleteUserAllFilesRequest(String userId) {
        log.info("Producing RabbitMQ delete all files request. userId: {}", userId);
        DeleteUserAllFilesMessage deleteUserAllFilesMessage = DeleteUserAllFilesMessage.of(userId);
        rabbitTemplate.convertAndSend(fileExchangeName, deleteUserAllFilesRoutingKey, deleteUserAllFilesMessage);
    }

    @Recover
    public void sendToDLX(Exception e, List<String> fileUrls, String userId) {
        log.warn("Failed to send message. Sending to DLX. userId: {}, fileUrls: {}", userId, fileUrls);
        log.error("Exception: {}", e.getMessage(), e);
        DeleteFilesMessage deleteFilesMessage = DeleteFilesMessage.of(fileUrls, userId);
        rabbitTemplate.convertAndSend(dlxExchangeName, dlxRoutingKey, deleteFilesMessage);
    }
}
