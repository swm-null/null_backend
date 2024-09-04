package com.example.oatnote.memoTag.service.client.models;

import jakarta.validation.constraints.NotBlank;

public record AICreateTagRequest(
    @NotBlank(message = "내용은 비워둘 수 없습니다.")
    String content
) {

    public static AICreateTagRequest from(String name) {
        return new AICreateTagRequest(name);
    }
}
