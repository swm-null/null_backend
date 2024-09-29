package com.example.oatnote.event;

import java.time.LocalDateTime;

import com.example.oatnote.memotag.service.client.dto.AICreateTagsResponse;
import com.example.oatnote.memotag.service.memo.model.Memo;

public record CreateStructureAsyncEvent(
    AICreateTagsResponse aiCreateTagsResponse,
    Memo rawMemo,
    String userId,
    LocalDateTime now
) {

}
