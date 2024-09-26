package com.example.oatnote.memotag.service.client.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.example.oatnote.memotag.service.client.dto.innerDto.ProcessedMemoTags;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AICreateStructureResponse(
    List<ProcessedMemo> processedMemos,
    TagsRelations tagsRelations,
    List<NewTag> newTags,
    Map<String, List<String>> newStructure
) {

    @JsonNaming(SnakeCaseStrategy.class)
    public record ProcessedMemo(
        String timestamp,
        String content,
        List<String> parentTagIds,
        List<Object> embedding
    ) {

    }

    @JsonNaming(SnakeCaseStrategy.class)
    public record TagsRelations(
        List<AddedRelation> added,
        List<DeletedRelation> deleted
    ) {

    }

    @JsonNaming(SnakeCaseStrategy.class)
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

    @JsonNaming(SnakeCaseStrategy.class)
    public record NewTag(
        String id,
        String name,
        boolean isNew,
        List<Object> embedding
    ) {

    }
}
