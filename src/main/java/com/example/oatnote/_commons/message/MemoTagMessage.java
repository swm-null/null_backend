package com.example.oatnote._commons.message;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.example.oatnote.domain.memotag.service.client.dto.AiCreateTagsResponse;
import com.example.oatnote.domain.memotag.service.memo.model.Memo;

public record MemoTagMessage(
    AiCreateTagsResponse aiCreateTagsResponse,
    Memo rawMemo,
    String userId,
    LocalDateTime time
) implements Serializable {

}
