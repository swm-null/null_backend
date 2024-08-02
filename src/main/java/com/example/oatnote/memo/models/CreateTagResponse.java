package com.example.oatnote.memo.models;

import com.example.oatnote.memo.models.InnerResponse.TagResponse;
import com.example.oatnote.memo.service.tag.models.Tag;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateTagResponse(
    @Schema(description = "태그")
    TagResponse tag
) {

    public static CreateTagResponse from(Tag tag) {
        return new CreateTagResponse(
            new TagResponse(
                tag.getId(),
                tag.getName()
            )
        );
    }
}
