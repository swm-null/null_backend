package com.example.oatnote.domain.file.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.example.oatnote._commons.message.DeleteFilesMessage;
import com.example.oatnote.domain.file.service.FileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteFilesMessageConsumer {

    private final FileService fileService;

    @RabbitListener(queues = "${spring.rabbitmq.queues.file.queue}")
    public void receiveFilesMessage(@Payload DeleteFilesMessage message) {
        log.info("Received delete files request for userId: {}", message.userId());

        try {
            fileService.deleteFiles(message);
            log.info("Files deletion processed successfully - userId: {}", message.userId());
        } catch (Exception e) {
            log.error("Error processing files deletion - userId: {}", message.userId(), e);
        }
    }
}
