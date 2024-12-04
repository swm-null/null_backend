package com.example.oatnote.file.dto;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(SnakeCaseStrategy.class)
public record UploadFilesResponse(
    @Schema(description = "파일 URLs", example = "\"https://example.com/file1.jpg\", \"https://example.com/file2.jpg\"]")
    List<String> fileUrls
) {

    public static UploadFilesResponse of(List<String> fileUrls) {
        return new UploadFilesResponse(fileUrls);
    }
}
