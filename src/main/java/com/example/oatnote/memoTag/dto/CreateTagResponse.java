package com.example.oatnote.memoTag.dto;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import com.example.oatnote.memoTag.dto.InnerResponse.TagResponse;
import com.example.oatnote.memoTag.service.tag.model.Tag;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(SnakeCaseStrategy.class)
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
