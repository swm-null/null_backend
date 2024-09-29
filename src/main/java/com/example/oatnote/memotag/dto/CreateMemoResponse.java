package com.example.oatnote.memotag.dto;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import java.util.List;

import com.example.oatnote.memotag.dto.innerDto.MemoResponse;
import com.example.oatnote.memotag.service.client.dto.innerDto.RawTag;
import com.example.oatnote.memotag.service.memo.model.Memo;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(SnakeCaseStrategy.class)
public record CreateMemoResponse(
    @Schema(description = "메모")
    MemoResponse memo
) {

    public static CreateMemoResponse from(Memo memo, List<RawTag> tags) {
        return new CreateMemoResponse(
            MemoResponse.fromRawTag(memo, tags)
        );
    }
}
