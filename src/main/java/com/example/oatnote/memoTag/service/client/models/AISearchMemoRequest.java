package com.example.oatnote.memoTag.service.client.models;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotBlank;

@JsonNaming(SnakeCaseStrategy.class)
public record AISearchMemoRequest(
    @NotBlank(message = "내용은 비워둘 수 없습니다.")
    String content
) {

    public static AISearchMemoRequest from(String content) {
        return new AISearchMemoRequest(content);
    }
}
