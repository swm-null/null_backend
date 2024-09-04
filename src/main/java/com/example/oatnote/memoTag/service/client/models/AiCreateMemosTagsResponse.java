package com.example.oatnote.memoTag.service.client.models;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;

public record AiCreateMemosTagsResponse(
    List<AiMemosTagsResponse> processedMemos
) {

    public record AiMemosTagsResponse(
        String content,
        List<Double> embedding,
        String timestamp,
        List<String> parentTagIds,
        List<AiCreateMemoTagsResponse.AiMemoTagsResponse.NewTag> newTags,
        AiCreateMemoTagsResponse.AiMemoTagsResponse.TagRelations tagRelations
    ) {

        public record NewTag(
            String id,
            String name,
            List<Double> embedding
        ) {

        }

        public record TagRelations(
            List<AiCreateMemoTagsResponse.AiMemoTagsResponse.TagRelations.Relation> added,
            List<AiCreateMemoTagsResponse.AiMemoTagsResponse.TagRelations.Relation> deleted
        ) {
            public record Relation(
                String parentId,
                String childId
            ) {

            }
        }
    }
}
