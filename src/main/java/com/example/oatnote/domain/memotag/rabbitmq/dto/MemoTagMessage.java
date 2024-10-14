package com.example.oatnote.domain.memotag.rabbitmq.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.example.oatnote.domain.memotag.service.client.dto.AICreateTagsResponse;
import com.example.oatnote.domain.memotag.service.memo.model.Memo;

public record MemoTagMessage(
    AICreateTagsResponse aiCreateTagsResponse,
    Memo rawMemo,
    String userId,
    LocalDateTime time
) implements Serializable {

}
