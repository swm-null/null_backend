package com.example.oatnote.domain.memotag.dto;

import com.example.oatnote.domain.memotag.dto.innerDto.MemoResponse;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(SnakeCaseStrategy.class)
public record UpdateMemoTagsResponse(
    @Schema(description = "업데이트된 메모")
    MemoResponse memo
) {

}
