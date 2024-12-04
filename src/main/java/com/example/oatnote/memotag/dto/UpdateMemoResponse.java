package com.example.oatnote.memotag.dto;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import com.example.oatnote.memotag.dto.innerDto.MemoResponse;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(SnakeCaseStrategy.class)
public record UpdateMemoResponse(
    @Schema(description = "메모")
    MemoResponse memo
) {

    public static UpdateMemoResponse from(MemoResponse memo) {
        return new UpdateMemoResponse(
            memo
        );
    }
}
