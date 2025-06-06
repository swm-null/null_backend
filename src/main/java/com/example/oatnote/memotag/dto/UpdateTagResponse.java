package com.example.oatnote.memotag.dto;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import com.example.oatnote.memotag.dto.innerDto.TagResponse;
import com.example.oatnote.memotag.service.tag.model.Tag;
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
