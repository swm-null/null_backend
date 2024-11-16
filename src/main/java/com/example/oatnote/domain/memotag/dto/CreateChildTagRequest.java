package com.example.oatnote.domain.memotag.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import java.util.List;

import com.example.oatnote.domain.memotag.service.tag.model.Tag;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(SnakeCaseStrategy.class)
public record CreateChildTagRequest(
    @Schema(description = "태그 이름", example = "일정", requiredMode = REQUIRED)
    String name
) {

    public Tag toTag(String userId, List<Double> embedding) {
        return new Tag(
            null,
            name,
            userId,
            embedding
        );
    }
}
