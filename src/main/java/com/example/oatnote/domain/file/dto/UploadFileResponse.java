package com.example.oatnote.domain.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UploadFileResponse(
    @Schema(description = "파일 URL", example = "https://example.com/file.jpg")
    String url
) {

}
