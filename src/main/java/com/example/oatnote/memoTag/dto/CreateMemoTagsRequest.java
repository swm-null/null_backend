package com.example.oatnote.memoTag.dto;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import java.util.List;

import com.example.oatnote.memoTag.service.memo.model.Memo;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonNaming(SnakeCaseStrategy.class)
public record CreateMemoTagsRequest(
    @Schema(description = "내용", example = "내일은 5시 멘토링을 들어야해", requiredMode = REQUIRED)
    @NotBlank(message = "내용은 비워둘 수 없습니다.")
    String content,

    @Schema(
        description = "이미지 URL 리스트",
        example = "[\"https://example.com/image1.jpg\", \"https://example.com/image2.jpg\"]",
        requiredMode = REQUIRED
    )
    @NotNull(message = "이미지 URL 리스트는 null일 수 없습니다.")
    List<String> imageUrls
) {

}
