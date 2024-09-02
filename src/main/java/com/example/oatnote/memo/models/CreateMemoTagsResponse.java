package com.example.oatnote.memo.models;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import java.util.List;

import com.example.oatnote.memo.models.InnerResponse.MemoTagsResponse;
import com.example.oatnote.memo.service.memo.models.Memo;
import com.example.oatnote.memo.service.tag.models.Tag;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(SnakeCaseStrategy.class)
public record CreateMemoTagsResponse(
    @Schema(description = "메모")
    MemoTagsResponse memo
) {

    public static CreateMemoTagsResponse from(Memo memo, List<Tag> tags) {
        return new CreateMemoTagsResponse(
            MemoTagsResponse.from(memo, tags)
        );
    }
}