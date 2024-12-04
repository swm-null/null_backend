package com.example.oatnote.file.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@JsonNaming(SnakeCaseStrategy.class)
public record UploadFileResponse(
    @Schema(description = "파일 URL", example = "https://example.com/file.jpg")
    @NotBlank(message = "fileUrl은 필수입니다.")
    String fileUrl
) {

    public static UploadFileResponse of(String fileUrl) {
        return new UploadFileResponse(fileUrl);
    }
}
