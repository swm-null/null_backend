package com.example.oatnote.memoTag.service.client.dto;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AICreateMemoTagsRequest(
    List<AIMemoTagsRequest> memos
) {

    private record AIMemoTagsRequest(
        String content
    ) {

        public static AIMemoTagsRequest from(String content) {
            return new AIMemoTagsRequest(content);
        }
    }

    public static AICreateMemoTagsRequest from(String content) {
        return new AICreateMemoTagsRequest(List.of(AIMemoTagsRequest.from(content)));
    }
}
