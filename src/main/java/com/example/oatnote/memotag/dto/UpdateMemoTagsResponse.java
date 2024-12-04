package com.example.oatnote.memotag.dto;

import java.util.List;

import com.example.oatnote.memotag.dto.innerDto.MemoResponse;
import com.example.oatnote.memotag.service.client.dto.innerDto.RawTag;
import com.example.oatnote.memotag.service.memo.model.Memo;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(SnakeCaseStrategy.class)
public record UpdateMemoTagsResponse(
    @Schema(description = "업데이트된 메모")
    MemoResponse memo
) {

    public static UpdateMemoTagsResponse from(Memo memo, List<RawTag> tags) {
        return new UpdateMemoTagsResponse(
            MemoResponse.fromRawTag(memo, tags)
        );
    }
}
