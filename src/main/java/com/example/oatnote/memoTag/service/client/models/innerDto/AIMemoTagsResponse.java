package com.example.oatnote.memoTag.service.client.models.innerDto;

import java.util.List;

import com.example.oatnote.memoTag.service.client.models.AICreateMemosTagsResponse;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AIMemoTagsResponse(
    String content,
    List<Double> embedding,
    String timestamp,
    List<String> parentTagIds,
    List<AICreateMemosTagsResponse.AIMemoTagsResponse.NewTag> newTags,
    AICreateMemosTagsResponse.AIMemoTagsResponse.TagRelations tagRelations
) {

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record NewTag(
        String id,
        String name,
        List<Double> embedding
    ) {

    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record TagRelations(
        List<AICreateMemosTagsResponse.AIMemoTagsResponse.TagRelations.Relation> added,
        List<AICreateMemosTagsResponse.AIMemoTagsResponse.TagRelations.Relation> deleted
    ) {

        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public record Relation(
            String parentId,
            String childId
        ) {

        }
    }
}
