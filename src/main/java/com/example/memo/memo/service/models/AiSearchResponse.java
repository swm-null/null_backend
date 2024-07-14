package com.example.memo.memo.service.models;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import java.util.List;

import org.bson.types.ObjectId;

import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AiSearchResponse(
    String type, // "similarity" | "regex" | "tag" | "unspecified"
    String processedMessage,
    List<ObjectId> ids,
    String regex,
    List<ObjectId> tags
) {

}
