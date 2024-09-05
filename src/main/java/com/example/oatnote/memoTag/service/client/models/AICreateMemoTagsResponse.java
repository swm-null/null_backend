package com.example.oatnote.memoTag.service.client.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AICreateMemoTagsResponse(
    AICreateMemosTagsResponse.AIMemoTagsResponse processedMemos
) {

}
