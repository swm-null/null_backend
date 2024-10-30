package com.example.oatnote.domain.memotag.service.client.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.example.oatnote.domain.memotag.service.memo.model.Memo;
import com.example.oatnote.domain.memotag.service.tag.model.Tag;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AICreateStructureResponse(
    List<NewTag> newTags,
    List<ProcessedMemo> processedMemos,
    Map<String, List<String>> newStructure,
    Map<String, List<String>> newReversedStructure
) {

    @JsonNaming(SnakeCaseStrategy.class)
    public record ProcessedMemo(
        String content,
        List<String> imageUrls,
        String metadata,
        List<Double> embedding,
        List<Double> embeddingMetadata,
        LocalDateTime timestamp,
        List<String> parentTagIds
    ) {

    }

    @JsonNaming(SnakeCaseStrategy.class)
    public record NewTag(
        String id,
        String name,
        boolean isNew,
        List<Double> embedding
    ) {

        public Tag toTag(String userId, LocalDateTime time) {
            return new Tag(
                id,
                name,
                userId,
                embedding,
                time
            );
        }
    }
}
