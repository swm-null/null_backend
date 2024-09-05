package com.example.oatnote.memoTag.service.client.models;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AICreateMemosTagsResponse(
    List<AIMemoTagsResponse> processedMemos
) {

    @JsonNaming(SnakeCaseStrategy.class)
    public record AIMemoTagsResponse(
        String content,
        List<Double> embedding,
        String timestamp,
        List<String> parentTagIds,
        List<NewTag> newTags,
        TagRelations tagRelations
    ) {

        @JsonNaming(SnakeCaseStrategy.class)
        public record NewTag(
            String id,
            String name,
            List<Double> embedding
        ) {

        }

        @JsonNaming(SnakeCaseStrategy.class)
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
}
