package com.example.oatnote.memoTag.dto;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import java.util.List;

import com.example.oatnote.memoTag.service.tag.model.Tag;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(SnakeCaseStrategy.class)
public record CreateTagRequest(
    @Schema(description = "태그 이름", example = "일정")
    String name
) {

    public Tag toTag(List<Double> embedding) {
        return Tag.builder()
            .name(name)
            .embedding(embedding)
            .build();
    }
}
