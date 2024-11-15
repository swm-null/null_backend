package com.example.oatnote.domain.memotag.service.aiClient.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.oatnote.domain.memotag.service.aiClient.dto.innerDto.RawTag;
import com.example.oatnote.domain.memotag.service.memo.model.Memo;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AiCreateTagsResponse(
    List<RawTag> tags,
    String metadata
) {

    public AiCreateStructureRequest toAiCreateStructureRequest(Memo memo, String userId) {
        AiCreateStructureRequest.RawMemo rawMemo = new AiCreateStructureRequest.RawMemo(
            memo.getContent(),
            memo.getImageUrls(),
            memo.getVoiceUrls(),
            memo.getMetadata(),
            LocalDateTime.now(),
            tags
        );

        return new AiCreateStructureRequest(
            List.of(rawMemo),
            null,
            null,
            userId
        );
    }
}
