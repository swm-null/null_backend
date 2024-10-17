package com.example.oatnote.domain.memotag.dto;

import static com.example.oatnote.web.validation.enums.AllowedFileTypeEnum.*;
import static com.example.oatnote.web.validation.enums.AllowedFileTypeEnum.SVG;
import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import com.example.oatnote.web.validation.AllowedFileType;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@JsonNaming(SnakeCaseStrategy.class)
public record CreateMemosRequest(
    @Schema(description = "파일 url", example = "https://example.com/kakao.csv")
    @NotBlank(message = "파일 url은 비워둘 수 없습니다.")
    @AllowedFileType({TXT, SVG})
    String content
) {

}
