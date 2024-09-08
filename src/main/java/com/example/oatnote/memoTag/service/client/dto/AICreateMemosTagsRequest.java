package com.example.oatnote.memoTag.service.client.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AICreateMemosTagsRequest(
    String content,
    String type,
    String userId
) {
    public static AICreateMemosTagsRequest from(String content, String type, String userId) {
        return new AICreateMemosTagsRequest(content, type, userId);
    }
}
