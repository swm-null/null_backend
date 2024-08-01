package com.example.memo.memo.models;

import com.example.memo.memo.service.models.Tag;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateTagResponse(
    @Schema(description = "태그", example = """
        {"id": "60c72b3e9b1e8b1e4c8b4568", "name": "일정"},
        """)
    TagResponse tag
) {

    public static UpdateTagResponse from(Tag tag) {
        return new UpdateTagResponse(
            new TagResponse(
                tag.getId(),
                tag.getName()
            )
        );
    }
}
