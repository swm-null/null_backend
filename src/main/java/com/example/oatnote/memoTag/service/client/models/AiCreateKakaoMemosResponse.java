package com.example.oatnote.memoTag.service.client.models;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;

public record AiCreateKakaoMemosResponse(
    @NotEmpty(message = "카카오톡 메모가 비어있습니다.")
    List<AiCreateMemoResponse> kakao
) {

    public static AiCreateKakaoMemosResponse from(List<AiCreateMemoResponse> kakaoMemos) {
        return new AiCreateKakaoMemosResponse(kakaoMemos);
    }
}
