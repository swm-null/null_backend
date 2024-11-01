package com.example.oatnote.domain.memotag.service.client.dto;

import java.util.List;

import com.example.oatnote.domain.memotag.service.client.dto.innerDto.RawTag;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AiCreateStructureRequest(
    List<RawMemo> memos,
    String userId
) {

    @JsonNaming(SnakeCaseStrategy.class)
    public record RawMemo(
        String content,
        List<String> imageUrls,
        List<RawTag> tags
    ) {

    }
}
