package com.example.oatnote.memoTag.service.client.dto;

import java.util.List;
import java.util.Map;

import com.example.oatnote.memoTag.service.client.dto.innerDto.ProcessedMemoResponse;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AICreateMemoResponse(
    ProcessedMemoResponse processedMemo,
    Map<String, List<String>> newStructure
) {

}
