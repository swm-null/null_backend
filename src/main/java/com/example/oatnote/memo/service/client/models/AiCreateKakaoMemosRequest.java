package com.example.oatnote.memo.service.client.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AiCreateKakaoMemosRequest(
    @NotBlank(message = "타입은 비워둘 수 없습니다.")
    @Pattern(regexp = "csv|txt", message = "타입은 'csv' 또는 'txt'여야 합니다.")
    String type,

    @NotBlank(message = "내용은 비워둘 수 없습니다.")
    String content
) {
    public static AiCreateKakaoMemosRequest from(String type, String content) {
        return new AiCreateKakaoMemosRequest(type, content);
    }
}
