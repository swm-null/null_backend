package com.example.oatnote.domain.file.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.example.oatnote._commons.message.DeleteUserAllFilesMessage;
import com.example.oatnote._commons.message.DeleteFilesMessage;
import com.example.oatnote.domain.file.service.FileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilesMessageConsumer {

    private final FileService fileService;

    @RabbitListener(queues = "${spring.rabbitmq.queues.file.delete.queue}")
    public void receiveDeleteFilesMessage(@Payload DeleteFilesMessage deleteFilesMessage) {
        log.info("Received delete files request for userId: {}", deleteFilesMessage.userId());

        try {
            fileService.deleteFiles(deleteFilesMessage);
            log.info("Files deletion processed successfully - userId: {}", deleteFilesMessage.userId());
        } catch (Exception e) {
            log.error("Error processing files deletion - userId: {}", deleteFilesMessage.userId(), e);
        }
    }

    @RabbitListener(queues = "${spring.rabbitmq.queues.file.delete-all.queue}")
    public void receiveDeleteAllFilesMessage(@Payload DeleteUserAllFilesMessage deleteUserAllFilesMessage) {
        log.info("Received delete all files request for userId: {}", deleteUserAllFilesMessage.userId());

        try {
            fileService.deleteUserAllFiles(deleteUserAllFilesMessage);
            log.info("All files deletion processed successfully - userId: {}", deleteUserAllFilesMessage.userId());
        } catch (Exception e) {
            log.error("Error processing all files deletion - userId: {}", deleteUserAllFilesMessage.userId(), e);
        }
    }
}
