package com.example.oatnote.memoTag.service.client.models;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AICreateMemoTagsResponse(
    List<AIMemoTagsResponse> processedMemos
) {

    public record AIMemoTagsResponse(
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
}
