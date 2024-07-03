package com.example.memo.memo.models;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import java.util.List;

import com.example.memo.memo.service.models.MemoResponseBridge;

import io.swagger.v3.oas.annotations.media.Schema;

public record MemoResponse(
    @Schema(description = "메모 고유 ID", example = "1")
    String id,

    @Schema(description = "내용", example = "text")
    String content,

    @Schema(description = "태그", example = """
        ["tag1", "tag2"]
        """, requiredMode = REQUIRED)
    List<String> tags
) {

    public static MemoResponse from(MemoResponseBridge memoResponseBridge) {
        return new MemoResponse(
            memoResponseBridge.getId(),
            memoResponseBridge.getContent(),
            memoResponseBridge.getTags()
        );
    }
}
