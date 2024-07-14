package com.example.memo.memo.models;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import java.util.List;

import org.bson.types.ObjectId;

import com.example.memo.memo.service.models.Memo;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(SnakeCaseStrategy.class)
public record UpdateMemoResponse(
    @Schema(description = "메모 고유 ID", example = "61b72b3e9b1e8b1e4c8b4560")
    ObjectId id,

    @Schema(description = "내용", example = "소마 근처 맛집 : 맥도날드")
    String content,

    @Schema(description = "태그", example = """
        ["장소", "맛집"]
        """)
    List<ObjectId> tags
) {

    public static UpdateMemoResponse from(Memo memo) {
        return new UpdateMemoResponse(
            memo.getId(),
            memo.getContent(),
            memo.getTags()
        );
    }
}
