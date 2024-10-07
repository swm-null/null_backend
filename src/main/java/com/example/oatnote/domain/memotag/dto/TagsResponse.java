package com.example.oatnote.domain.memotag.dto;

import java.util.List;

import com.example.oatnote.domain.memotag.dto.innerDto.TagResponse;
import com.example.oatnote.domain.memotag.service.tag.model.Tag;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(SnakeCaseStrategy.class)
public record TagsResponse(
    @Schema(description = "태그")
    TagResponse tag,

    @Schema(description = "자식 태그 리스트")
    List<TagResponse> childTags
) {

    public TagsResponse from(
        Tag tag,
        List<Tag> childTags
    ) {
        return new TagsResponse(
            TagResponse.fromTag(tag),
            childTags.stream().map(TagResponse::fromTag).toList()
        );
    }
}
