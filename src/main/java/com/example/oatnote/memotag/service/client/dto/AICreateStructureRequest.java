package com.example.oatnote.memotag.service.client.dto;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AICreateStructureRequest(
    List<AIMemo> memos,
    String userId
) {

    private record AIMemo(
        String content,
        List<AITag> tags
    ) {

        private record AITag(
            String id,
            String name,
            boolean isNew
        ) {

        }
    }
}
