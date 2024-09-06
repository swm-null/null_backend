package com.example.oatnote.memoTag.service.client.dto.innerDto;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ProcessedMemoResponse(
    String content,
    List<Double> embedding,
    String timestamp,
    List<UUID> parentTagIds,
    List<NewTag> newTags,
    TagRelations tagRelations
) {

    public record NewTag(
        UUID id,
        String name,
        List<Double> embedding
    ) {

    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record TagRelations(
        List<Relation> added,
        List<Relation> deleted
    ) {

        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public record Relation(
            UUID parentId,
            UUID childId
        ) {

        }
    }
}
