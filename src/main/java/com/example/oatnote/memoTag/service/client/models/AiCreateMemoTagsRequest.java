package com.example.oatnote.memoTag.service.client.models;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AiCreateMemoTagsRequest(
    List<AiMemoTagsRequest> memos
) {

    private record AiMemoTagsRequest(
        String content,

        LocalDateTime timestamp
    ) {

        public static AiMemoTagsRequest from(String content, LocalDateTime timestamp) {
            return new AiMemoTagsRequest(content, timestamp);
        }
    }

    public static AiCreateMemoTagsRequest from(String content, LocalDateTime timestamp) {
        return new AiCreateMemoTagsRequest(List.of(AiMemoTagsRequest.from(content, timestamp)));
    }
}
