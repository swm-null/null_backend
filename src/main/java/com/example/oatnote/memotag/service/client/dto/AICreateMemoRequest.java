package com.example.oatnote.memotag.service.client.dto;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AICreateMemoRequest(
    AIMemo memo,
    String userId
) {

    private record AIMemo(
        String content
    ) {

        public static AIMemo from(String content) {
            return new AIMemo(content);
        }
    }

    public static AICreateMemoRequest from(String content, String userId) {
        return new AICreateMemoRequest(AIMemo.from(content), userId);
    }
}
