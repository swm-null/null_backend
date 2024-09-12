package com.example.oatnote.memotag.dto;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@JsonNaming(SnakeCaseStrategy.class)
public record CreateMemosRequest(
    @Schema(description = "파일 url", example = "https://example.com/kakao.csv")
    @Pattern(regexp = ".*\\.(csv|txt)$", message = "파일 확장자는 .csv 또는 .txt이어야 합니다.")
    @NotBlank(message = "파일 url은 비워둘 수 없습니다.")
    String content
) {

}
