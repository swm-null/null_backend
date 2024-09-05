package com.example.oatnote.memoTag.service.client.dto;

import com.example.oatnote.memoTag.service.client.dto.innerDto.ProcessedMemoResponse;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AICreateMemoTagsResponse(
    ProcessedMemoResponse processedMemo
) {

}
