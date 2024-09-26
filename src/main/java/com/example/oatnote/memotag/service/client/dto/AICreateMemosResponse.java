package com.example.oatnote.memotag.service.client.dto;

import java.util.List;
import java.util.Map;

import com.example.oatnote.memotag.service.client.dto.innerDto.ProcessedMemoTags;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AICreateMemosResponse(
    List<ProcessedMemoTags> processedMemoTags,
    Map<String, List<String>> newStructure
) {
}
