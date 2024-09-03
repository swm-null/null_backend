package com.example.oatnote.memoTag.dto;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import java.util.List;

import com.example.oatnote.memoTag.dto.InnerResponse.MemoTagsResponse;
import com.example.oatnote.memoTag.service.memo.model.Memo;
import com.example.oatnote.memoTag.service.tag.model.Tag;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(SnakeCaseStrategy.class)
public record UpdateMemoResponse(
    @Schema(description = "메모")
    MemoTagsResponse memo
) {

    public static UpdateMemoResponse from(Memo memo, List<Tag> tags) {
        return new UpdateMemoResponse(
            MemoTagsResponse.from(memo, tags)
        );
    }
}
