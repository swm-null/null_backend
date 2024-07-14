package com.example.memo.memo.models;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import java.util.List;

import com.example.memo.memo.service.models.Memo;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import org.bson.types.ObjectId;

@JsonNaming(SnakeCaseStrategy.class)
public record UpdateMemoResponse(
    @Schema(description = "메모 고유 ID", example = "1")
    ObjectId id,

    @Schema(description = "내용", example = "text")
    String content,

    @Schema(description = "태그", example = """
        ["tag1", "tag2"]
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
