package com.example.oatnote.memo.service.client.models;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@JsonNaming(SnakeCaseStrategy.class)
public record AiCreateMemoResponse(
    @NotEmpty(message = "임베딩값은 비워둘 수 없습니다.")
    List<Double> memoEmbeddings,

    List<String> existingTagIds,

    List<InnerTag> newTags,

    LocalDateTime timestamp
) {

    public record InnerTag(
        String id,
        String name,
        String parent,
        List<Double> embedding
    ) {

    }
}
