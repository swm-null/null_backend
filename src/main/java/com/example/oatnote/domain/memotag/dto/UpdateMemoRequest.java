package com.example.oatnote.domain.memotag.dto;

import static com.example.oatnote.web.validation.enums.AllowedFileTypeEnum.IMAGE;
import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import java.util.List;

import com.example.oatnote.web.validation.AllowedFileType;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@JsonNaming(SnakeCaseStrategy.class)
public record UpdateMemoRequest(
    @Schema(description = "내용", example = "내일 5시로 멘토링이 변경되었다.", requiredMode = REQUIRED)
    @NotNull(message = "내용은 null일 수 없습니다.")
    String content,

    @Schema(
        description = "이미지 URL 리스트",
        example = "[\"https://example.com/image1.jpg\", \"https://example.com/image2.jpg\"]",
        requiredMode = REQUIRED
    )
    @NotNull(message = "이미지 URL 리스트는 null일 수 없습니다.")
    @Size(max = 5, message = "이미지 URL 리스트는 5개 이하로 입력해주세요.")
    @AllowedFileType(IMAGE)
    List<String> imageUrls
) {

}
