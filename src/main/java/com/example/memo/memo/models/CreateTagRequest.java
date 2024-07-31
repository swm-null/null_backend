package com.example.memo.memo.models;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateTagRequest(
    @Schema(description = "태그 이름", example = "일정")
    String name
) {

}
