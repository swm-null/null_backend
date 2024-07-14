package com.example.memo.memo.models;

import java.util.List;

import org.bson.types.ObjectId;

import com.example.memo.memo.service.models.Memo;

import io.swagger.v3.oas.annotations.media.Schema;

public record MemoResponse(
    @Schema(description = "메모 고유 ID", example = "61b72b3e9b1e8b1e4c8b4560")
    ObjectId id,

    @Schema(description = "내용", example = "내일은 5시 멘토링을 들어야해")
    String content,

    @Schema(description = "태그", example = """
        ["일정", "멘토링"]
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
