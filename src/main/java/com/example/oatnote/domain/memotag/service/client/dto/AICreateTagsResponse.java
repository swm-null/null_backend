package com.example.oatnote.domain.memotag.service.client.dto;

import java.util.List;

import com.example.oatnote.domain.memotag.service.client.dto.innerDto.RawTag;
import com.example.oatnote.domain.memotag.service.memo.model.Memo;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AICreateTagsResponse(
    List<RawTag> tags
) {

    public AICreateStructureRequest toAICreateStructureRequest(Memo memo, String userId) {
        AICreateStructureRequest.RawMemo rawMemo = new AICreateStructureRequest.RawMemo(
            memo.getContent(),
            memo.getImageUrls(),
            tags
        );

        return new AICreateStructureRequest(
            List.of(rawMemo),
            userId
        );
    }
}
