package com.example.oatnote.memotag.service.client.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.example.oatnote.memotag.service.client.dto.innerDto.ProcessedMemoTags;
import com.example.oatnote.memotag.service.tag.model.Tag;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AICreateTagsResponse(
    List<AITag> tags
) {

    public List<Tag> toTags(String userId, LocalDateTime now) {
        return tags.stream()
            .map(tag -> new Tag(
                tag.id(),
                tag.name(),
                userId,
                now
            ))
            .toList();
    }

    @JsonNaming(SnakeCaseStrategy.class)
    public record AITag(
        String id,
        String name,
        boolean isNew
    ) {

    }
}
