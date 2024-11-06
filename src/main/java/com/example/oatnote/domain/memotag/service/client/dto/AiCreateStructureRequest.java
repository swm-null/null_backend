package com.example.oatnote.domain.memotag.service.client.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.oatnote.domain.memotag.service.client.dto.innerDto.RawTag;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AiCreateStructureRequest(
    List<RawMemo> memos,
    String content,
    String type,
    String userId
) {

    public static AiCreateStructureRequest from(String fileUrl, String type, String userId) {
        return new AiCreateStructureRequest(
            null,
            fileUrl,
            type,
            userId
        );
    }

    @JsonNaming(SnakeCaseStrategy.class)
    public record RawMemo(
        String content,
        List<String> imageUrls,
        List<String> voiceUrls,
        LocalDateTime timestamp,
        List<RawTag> tags
    ) {

    }
}
