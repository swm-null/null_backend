package com.example.oatnote.memoTag.service.client.dto.innerDto;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ProcessedMemoResponse(
    String content,
    List<Double> embedding,
    String timestamp,
    List<String> parentTagIds,
    List<NewTag> newTags,
    TagRelations tagRelations
) {

    public record NewTag(
        String id,
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
            String parentId,
            String childId
        ) {

        }
    }
}
