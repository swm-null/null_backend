package com.example.oatnote.domain.memotag.dto.innerDto;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import com.example.oatnote.domain.memotag.service.client.dto.innerDto.RawTag;
import com.example.oatnote.domain.memotag.service.tag.model.Tag;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(SnakeCaseStrategy.class)
public record TagResponse(
    @Schema(description = "태그 ID", example = "60c72b3e9b1e8b1e4c8b4568")
    String id,

    @Schema(description = "태그 이름", example = "일정")
    String name
) {

    public static TagResponse fromRawTag(RawTag tag) {
        return new TagResponse(tag.id(), tag.name());
    }

    public static TagResponse fromTag(Tag tag) {
        return new TagResponse(tag.getId(), tag.getName());
    }
}
