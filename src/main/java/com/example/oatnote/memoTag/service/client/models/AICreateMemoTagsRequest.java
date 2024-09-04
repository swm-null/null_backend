package com.example.oatnote.memoTag.service.client.models;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AICreateMemoTagsRequest(
    List<AIMemoTagsRequest> memos
) {

    private record AIMemoTagsRequest(
        String content,

        LocalDateTime timestamp
    ) {

        public static AIMemoTagsRequest from(String content, LocalDateTime timestamp) {
            return new AIMemoTagsRequest(content, timestamp);
        }
    }

    public static AICreateMemoTagsRequest from(String content, LocalDateTime timestamp) {
        return new AICreateMemoTagsRequest(List.of(AIMemoTagsRequest.from(content, timestamp)));
    }
}
