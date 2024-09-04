package com.example.oatnote.memoTag.service.client.models;

import java.util.List;

public record AICreateMemosTagsResponse(
    List<AIMemosTagsResponse> processedMemos
) {

    public record AIMemosTagsResponse(
        String content,
        List<Double> embedding,
        String timestamp,
        List<String> parentTagIds,
        List<AICreateMemoTagsResponse.AIMemoTagsResponse.NewTag> newTags,
        AICreateMemoTagsResponse.AIMemoTagsResponse.TagRelations tagRelations
    ) {

        public record NewTag(
            String id,
            String name,
            List<Double> embedding
        ) {

        }

        public record TagRelations(
            List<AICreateMemoTagsResponse.AIMemoTagsResponse.TagRelations.Relation> added,
            List<AICreateMemoTagsResponse.AIMemoTagsResponse.TagRelations.Relation> deleted
        ) {
            public record Relation(
                String parentId,
                String childId
            ) {

            }
        }
    }
}
