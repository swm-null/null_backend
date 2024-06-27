package com.example.memo.memo.models;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.example.memo.memo.service.models.MemoRequestBridge;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record MemoRequest(
    @Schema(description = "내용", example = "text", requiredMode = REQUIRED)
    @NotBlank(message = "내용은 비워둘 수 없습니다.")
    String content
) {

    public MemoRequestBridge toMemoRequestBridge(MemoRequest memoRequest) {
        return new MemoRequestBridge(memoRequest.content());
    }
}
