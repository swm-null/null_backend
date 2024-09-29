package com.example.oatnote.memotag.dto;

import java.util.List;

import com.example.oatnote.memotag.dto.innerDto.TagResponse;
import com.example.oatnote.memotag.service.tag.model.Tag;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(SnakeCaseStrategy.class)
public record PagedTagsResponse(
    @Schema(description = "태그")
    TagResponse tag,

    @Schema(description = "자식 태그 리스트")
    List<TagResponse> childTags
) {

    public PagedTagsResponse from(
        Tag tag,
        List<Tag> childTags
    ) {
        return new PagedTagsResponse(
            TagResponse.fromTag(tag),
            childTags.stream().map(TagResponse::fromTag).toList()
        );
    }
}
