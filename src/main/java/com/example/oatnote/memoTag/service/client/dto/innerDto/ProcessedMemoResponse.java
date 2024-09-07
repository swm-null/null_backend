package com.example.oatnote.memoTag.service.client.dto.innerDto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ProcessedMemoResponse(
    String content,
    List<Double> embedding,
    String timestamp,
    List<String> parentTagIds,
    List<NewTag> newTags,
    TagRelations tagRelations,
    Map<String, List<String>> newStructure
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

        public record Relation(
            String parentId,
            String childId
        ) {

        }
    }
}
