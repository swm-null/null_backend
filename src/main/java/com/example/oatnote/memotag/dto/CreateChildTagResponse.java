package com.example.oatnote.memotag.dto;

import com.example.oatnote.memotag.dto.innerDto.TagResponse;
import com.example.oatnote.memotag.service.tag.model.Tag;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(SnakeCaseStrategy.class)
public record CreateChildTagResponse(
    @Schema(description = "태그")
    TagResponse tag
) {

    public static CreateChildTagResponse from(Tag tag) {
        return new CreateChildTagResponse(
            TagResponse.fromTag(tag)
        );
    }
}
