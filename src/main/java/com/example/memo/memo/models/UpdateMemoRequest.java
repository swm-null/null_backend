package com.example.memo.memo.models;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@JsonNaming(SnakeCaseStrategy.class)
public record UpdateMemoRequest(
    @Schema(description = "내용", example = "text", requiredMode = REQUIRED)
    @NotBlank(message = "내용은 비워둘 수 없습니다.")
    String content,

    @Schema(description = "태그", example = """
        ["tag1", "tag2"]
        """, requiredMode = REQUIRED)
    List<String> tags
) {

}