package com.example.oatnote.domain.memotag.rabbitmq;

import java.util.List;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.annotation.Recover;
import org.springframework.stereotype.Service;
import com.example.oatnote._commons.message.DeleteFilesMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteFilesMessageProducer {

    private final RabbitTemplate rabbitTemplate;

    private final String exchangeName = "file.exchange";
    private final String routingKey = "delete.files";

    @Retryable(
        retryFor = {Exception.class},
        maxAttempts = 2,
        backoff = @Backoff(delay = 1000)
    )
    public void sendDeleteFilesRequest(List<String> imageUrls, String userId) {
        log.info("Producing RabbitMQ delete files request. userId: {}", userId);
        DeleteFilesMessage deleteFilesMessage = DeleteFilesMessage.of(imageUrls, userId);
        rabbitTemplate.convertAndSend(exchangeName, routingKey, deleteFilesMessage);
    }

    @Recover
    public void sendToDLX(Exception e, List<String> imageUrls, String userId) {
        log.warn("Failed to send message. Sending to DLX. userId: {}, imageUrls: {}", userId, imageUrls);
        log.error("Exception: {}", e.getMessage(), e);
        DeleteFilesMessage deleteFilesMessage = DeleteFilesMessage.of(imageUrls, userId);
        rabbitTemplate.convertAndSend(exchangeName, routingKey, deleteFilesMessage);
    }
}
