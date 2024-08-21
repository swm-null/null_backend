package com.example.oatnote.memo.models;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@JsonNaming(SnakeCaseStrategy.class)
public record CreateKakaoMemosRequest(
    @Schema(description = "파일 url", example = "https://example.com/kakao.csv")
    @NotBlank(message = "파일 url은 비워둘 수 없습니다.")
    String content
) {

}
