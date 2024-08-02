package com.example.oatnote.memo.models;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateTagRequest(
    @Schema(description = "태그 이름", example = "일정")
    String name
) {

}
