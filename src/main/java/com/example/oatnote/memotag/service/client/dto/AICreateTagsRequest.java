package com.example.oatnote.memotag.service.client.dto;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AICreateTagsRequest(
    AIRawMemo rawMemo,
    String userId
) {

    public record AIRawMemo(
        String content
    ) {

    }
}
