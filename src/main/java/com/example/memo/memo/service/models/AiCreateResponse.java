package com.example.memo.memo.service.models;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AiCreateResponse(
    List<Double> memoEmbeddings,
    List<String> existingTagIds,
    List<InnerTag> newTags
) {

    public record InnerTag(
        String name,
        List<Double> embedding
    ) {

    }
}
