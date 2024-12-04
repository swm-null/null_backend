package com.example.oatnote.memo.service.client.dto;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AiCreateTagsRequest(
    RawMemo rawMemo,
    String userId
) {

    @JsonNaming(SnakeCaseStrategy.class)
    public record RawMemo(
        String content,
        List<String> imageUrls,
        List<String> voiceUrls
    ) {

    }
}
