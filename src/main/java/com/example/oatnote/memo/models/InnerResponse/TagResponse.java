package com.example.oatnote.memo.models.InnerResponse;

import com.example.oatnote.memo.service.tag.models.Tag;

import io.swagger.v3.oas.annotations.media.Schema;

public record TagResponse(
    @Schema(description = "태그 ID", example = "60c72b3e9b1e8b1e4c8b4568")
    String id,

    @Schema(description = "태그 이름", example = "일정")
    String name
) {

    public static TagResponse from(Tag tag) {
        return new TagResponse(tag.getId(), tag.getName());
    }
}