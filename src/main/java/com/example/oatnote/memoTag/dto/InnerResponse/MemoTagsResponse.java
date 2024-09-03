package com.example.oatnote.memoTag.dto.InnerResponse;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import java.time.LocalDateTime;
import java.util.List;

import com.example.oatnote.memoTag.service.memo.model.Memo;
import com.example.oatnote.memoTag.service.tag.model.Tag;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(SnakeCaseStrategy.class)
public record MemoTagsResponse(
    @Schema(description = "메모")
    MemoResponse memo,

    @Schema(description = "태그 리스트")
    List<TagResponse> tags
) {

    public static MemoTagsResponse from(Memo memo, List<Tag> tags) {
        return new MemoTagsResponse(
            MemoResponse.from(memo),
            tags.stream().map(TagResponse::from).toList()
        );
    }
}
