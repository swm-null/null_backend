package com.example.oatnote.domain.memotag.dto;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import java.util.List;

import com.example.oatnote.domain.memotag.dto.innerDto.MemoResponse;
import com.example.oatnote.domain.memotag.service.memo.model.Memo;
import com.example.oatnote.domain.memotag.service.tag.model.Tag;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(SnakeCaseStrategy.class)
public record UpdateMemoResponse(
    @Schema(description = "메모")
    MemoResponse memo
) {

    public static UpdateMemoResponse from(Memo memo, List<Tag> tags) {
        return new UpdateMemoResponse(
            MemoResponse.fromTag(memo, tags)
        );
    }
}
