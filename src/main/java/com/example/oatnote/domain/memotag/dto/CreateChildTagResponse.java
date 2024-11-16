package com.example.oatnote.domain.memotag.dto;

import com.example.oatnote.domain.memotag.dto.innerDto.TagResponse;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(SnakeCaseStrategy.class)
public record CreateChildTagResponse(
    @Schema(description = "태그")
    TagResponse tag
) {

}
