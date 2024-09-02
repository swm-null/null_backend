package com.example.oatnote.memoTag.models;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import com.example.oatnote.memoTag.models.InnerResponse.TagResponse;
import com.example.oatnote.memoTag.service.tag.models.Tag;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(SnakeCaseStrategy.class)
public record UpdateTagResponse(
    @Schema(description = "태그")
    TagResponse tag
) {

    public static UpdateTagResponse from(Tag tag) {
        return new UpdateTagResponse(
            new TagResponse(
                tag.getId(),
                tag.getName()
            )
        );
    }
}
