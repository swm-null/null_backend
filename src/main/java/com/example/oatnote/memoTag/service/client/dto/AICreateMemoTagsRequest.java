package com.example.oatnote.memoTag.service.client.dto;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AICreateMemoTagsRequest(
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

    public static AICreateMemoTagsRequest from(String content, String userId) {
        return new AICreateMemoTagsRequest(AIMemoTagsRequest.from(content), userId);
    }
}
