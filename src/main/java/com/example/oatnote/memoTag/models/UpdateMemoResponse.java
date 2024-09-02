package com.example.oatnote.memoTag.models;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import java.util.List;

import com.example.oatnote.memoTag.models.InnerResponse.MemoTagsResponse;
import com.example.oatnote.memoTag.service.memo.models.Memo;
import com.example.oatnote.memoTag.service.tag.models.Tag;
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
