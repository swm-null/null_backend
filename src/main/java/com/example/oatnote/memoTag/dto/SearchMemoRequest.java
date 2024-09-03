package com.example.oatnote.memoTag.dto;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@JsonNaming(SnakeCaseStrategy.class)
public record SearchMemoRequest(
    @Schema(description = "내용", example = "내 일정이 어떻게 되었더라?", requiredMode = REQUIRED)
    @NotBlank(message = "내용은 비워둘 수 없습니다.")
    String content
) {

}
