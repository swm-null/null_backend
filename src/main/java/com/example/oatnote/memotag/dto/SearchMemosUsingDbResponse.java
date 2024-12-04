package com.example.oatnote.memotag.dto;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import java.util.List;

import com.example.oatnote.memotag.dto.innerDto.MemoResponse;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(SnakeCaseStrategy.class)
public record SearchMemosUsingDbResponse(
    @Schema(description = "메모 리스트")
    List<MemoResponse> memos
) {

    public static SearchMemosUsingDbResponse from(List<MemoResponse> memos) {
        return new SearchMemosUsingDbResponse(
            memos
        );
    }
}
