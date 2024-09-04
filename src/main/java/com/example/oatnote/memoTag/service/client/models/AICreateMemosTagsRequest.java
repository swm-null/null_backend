package com.example.oatnote.memoTag.service.client.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@JsonNaming(SnakeCaseStrategy.class)
public record AICreateMemosTagsRequest(
    @NotBlank(message = "내용은 비워둘 수 없습니다.")
    String content,

    @NotBlank(message = "타입은 비워둘 수 없습니다.")
    @Pattern(regexp = "csv|txt", message = "타입은 'csv' 또는 'txt'여야 합니다.")
    String type
) {
    public static AICreateMemosTagsRequest from(String content, String type) {
        return new AICreateMemosTagsRequest(content, type);
    }
}
