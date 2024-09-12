package com.example.oatnote.memotag.service.client.dto;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AICreateMemoRequest(
    AIMemoTagsRequest memo,
    String userId
) {

    private record AIMemoTagsRequest(
        String content
    ) {

        public static AIMemoTagsRequest from(String content) {
            return new AIMemoTagsRequest(content);
        }
    }

    public static AICreateMemoRequest from(String content, String userId) {
        return new AICreateMemoRequest(AIMemoTagsRequest.from(content), userId);
    }
}
