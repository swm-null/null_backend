package com.example.oatnote.memoTag.dto;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(SnakeCaseStrategy.class)
public record UpdateTagRequest(
    @Schema(description = "태그 이름", example = "일정")
    String name
) {

}
