package com.example.oatnote.memo.service.client.dto;

import java.util.List;

import com.example.oatnote.memo.service.client.dto.innerDto.RawTag;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AiCreateTagsResponse(
    List<RawTag> tags,
    String metadata
) {

}
