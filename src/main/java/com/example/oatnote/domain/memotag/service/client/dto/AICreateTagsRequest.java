package com.example.oatnote.domain.memotag.service.client.dto;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AICreateTagsRequest(
    RawMemo rawMemo,
    String userId
) {

    public record RawMemo(
        String content,
        List<String> imageUrls
    ) {

    }
}
