package com.example.memo.memo.models;

import java.util.List;

import com.example.memo.memo.service.models.bridge.UpdateMemoResponseBridge;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateMemoResponse(
    @Schema(description = "메모 고유 ID", example = "1")
    String id,

    @Schema(description = "내용", example = "text")
    String content,

    @Schema(description = "태그", example = """
        ["tag1", "tag2"]
        """)
    List<String> tags
) {

    public static UpdateMemoResponse from(UpdateMemoResponseBridge updateMemoResponseBridge) {
        return new UpdateMemoResponse(
            updateMemoResponseBridge.getId(),
            updateMemoResponseBridge.getContent(),
            updateMemoResponseBridge.getTags()
        );
    }
}
