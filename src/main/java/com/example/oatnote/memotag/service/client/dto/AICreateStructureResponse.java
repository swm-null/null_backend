package com.example.oatnote.memotag.service.client.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.example.oatnote.memotag.service.client.dto.innerDto.ProcessedMemoTags;
import com.example.oatnote.memotag.service.memo.model.Memo;
import com.example.oatnote.memotag.service.tag.model.Tag;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AICreateStructureResponse(
    List<NewTag> newTags,
    List<ProcessedMemo> processedMemos,
    TagsRelations tagsRelations,
    Map<String, List<String>> newStructure
) {

    @JsonNaming(SnakeCaseStrategy.class)
    public record ProcessedMemo(
        String content,
        String metadata,
        List<Double> embedding,
        LocalDateTime timestamp,
        List<String> parentTagIds
    ) {

        public Memo toProcessedMemo(Memo memo) {
            return memo.process(content, metadata, embedding);
        }
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

    @JsonNaming(SnakeCaseStrategy.class)
    public record TagsRelations(
        List<AddedRelation> added,
        List<DeletedRelation> deleted
    ) {

        @JsonNaming(SnakeCaseStrategy.class) //todo toRelation
        public record AddedRelation(
            String parentId,
            String childId
        ) {

        }

        @JsonNaming(SnakeCaseStrategy.class)
        public record DeletedRelation(
            String parentId,
            String childId
        ) {

        }
    }
}
