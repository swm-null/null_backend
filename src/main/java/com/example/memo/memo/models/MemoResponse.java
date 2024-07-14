package com.example.memo.memo.models;

import java.util.List;

import org.bson.types.ObjectId;

import com.example.memo.memo.service.models.Memo;

import io.swagger.v3.oas.annotations.media.Schema;

public record MemoResponse(
    @Schema(description = "메모 고유 ID", example = "1")
    ObjectId id,

    @Schema(description = "내용", example = "text")
    String content,

    @Schema(description = "태그", example = """
        ["tag1", "tag2"]
        """)
    List<ObjectId> tags
) {

    public static MemoResponse from(Memo memo) {
        return new MemoResponse(
            memo.getId(),
            memo.getContent(),
            memo.getTags()
        );
    }
}
