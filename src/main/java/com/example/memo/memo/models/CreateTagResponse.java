package com.example.memo.memo.models;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateTagResponse(
    @Schema(description = "태그", example = """
        {"id": "60c72b3e9b1e8b1e4c8b4568", "name": "일정"},
        """)
    TagResponse tag
) {

}
