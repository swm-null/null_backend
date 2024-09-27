package com.example.oatnote.memotag.service.client.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.example.oatnote.memotag.service.client.dto.innerDto.AITag;
import com.example.oatnote.memotag.service.client.dto.innerDto.ProcessedMemoTags;
import com.example.oatnote.memotag.service.memo.model.Memo;
import com.example.oatnote.memotag.service.tag.model.Tag;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AICreateTagsResponse(
    List<AITag> tags
) {

    public AICreateStructureRequest toAICreateStructureRequest(Memo memo, String userId) {
        AICreateStructureRequest.AIMemo aiMemo = new AICreateStructureRequest.AIMemo(
            memo.getContent(),
            memo.getImageUrls(),
            tags
        );

        return new AICreateStructureRequest(
            List.of(aiMemo),
            userId
        );
    }
}
