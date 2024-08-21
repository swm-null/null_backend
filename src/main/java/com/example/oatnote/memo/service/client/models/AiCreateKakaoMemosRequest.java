package com.example.oatnote.memo.service.client.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AiCreateKakaoMemosRequest(
    @NotBlank(message = "내용은 비워둘 수 없습니다.")
    String content
) {
    public static AiCreateKakaoMemosRequest from(String content) {
        return new AiCreateKakaoMemosRequest(content);
    }
}
