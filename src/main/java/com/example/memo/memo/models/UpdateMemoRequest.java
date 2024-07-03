package com.example.memo.memo.models;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import java.util.List;

import com.example.memo.memo.service.models.bridge.UpdateMemoRequestBridge;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UpdateMemoRequest(
    @Schema(description = "내용", example = "text", requiredMode = REQUIRED)
    @NotBlank(message = "내용은 비워둘 수 없습니다.")
    String content,

    @Schema(description = "태그", example = """
        ["tag1", "tag2"]
        """, requiredMode = REQUIRED)
    List<String> tags
) {

    public UpdateMemoRequestBridge toUpdateMemoRequestBridge() {
        return UpdateMemoRequestBridge.builder()
            .content(content)
            .tags(tags)
            .build();
    }
}
