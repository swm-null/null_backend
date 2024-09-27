package com.example.oatnote.memotag.service.client.dto;

import java.util.List;

import com.example.oatnote.memotag.service.client.dto.innerDto.AITag;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AICreateStructureRequest(
    List<AIMemo> memos,
    String userId
) {

    @JsonNaming(SnakeCaseStrategy.class)
    public record AIMemo(
        String content,
        List<String> imageUrls,
        List<AITag> tags
    ) {

    }
}
