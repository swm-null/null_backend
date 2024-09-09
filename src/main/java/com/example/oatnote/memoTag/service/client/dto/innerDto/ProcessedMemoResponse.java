package com.example.oatnote.memoTag.service.client.dto.innerDto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record ProcessedMemoResponse(
    String content,
    List<Double> embedding,
    LocalDateTime timestamp,
    List<String> parentTagIds,
    List<NewTag> newTags,
    TagRelations tagsRelations
) {

    public record NewTag(
        String id,
        String name,
        List<Double> embedding
    ) {

    }

    public record TagRelations(
        List<Relation> added,
        List<Relation> deleted
    ) {

        @JsonNaming(SnakeCaseStrategy.class)
        public record Relation(
            String parentId,
            String childId
        ) {

        }
    }
}
