package com.example.oatnote.memo.models;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import java.util.List;

import com.example.oatnote.memo.models.InnerResponse.MemoResponse;
import com.example.oatnote.memo.models.InnerResponse.TagResponse;
import com.example.oatnote.memo.service.memo.models.Memo;
import com.example.oatnote.memo.service.tag.models.Tag;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(SnakeCaseStrategy.class)
public record CreateMemoResponse(
    @Schema(description = "메모")
    MemoResponse memo
) {

    public static CreateMemoResponse from(Memo memo, List<Tag> tags) {
        return new CreateMemoResponse(
            new MemoResponse(
                memo.getId(),
                memo.getContent(),
                tags.stream()
                    .map(tag -> new TagResponse(tag.getId(), tag.getName()))
                    .toList()
            )
        );
    }
}
