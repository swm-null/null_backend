package com.example.oatnote.memo.models.InnerResponse;

import java.util.List;

import com.example.oatnote.memo.service.memo.models.Memo;
import com.example.oatnote.memo.service.tag.models.Tag;

import io.swagger.v3.oas.annotations.media.Schema;

public record MemoResponse(
    @Schema(description = "메모 고유 ID", example = "61b72b3e9b1e8b1e4c8b4560")
    String id,

    @Schema(description = "내용", example = "내일은 5시 멘토링을 들어야해")
    String content,

    @Schema(description = "태그", example = """
        [
            {"id": "60c72b3e9b1e8b1e4c8b4568", "name": "일정"},
            {"id": "60c72b4f9b1e8b1e4c8b4569", "name": "멘토링"}
        ]
        """)
    List<TagResponse> tags
) {

    public static MemoResponse from(Memo memo, List<Tag> tags) {
        return new MemoResponse(
            memo.getId(),
            memo.getContent(),
            tags.stream()
                .map(tag -> new TagResponse(tag.getId(), tag.getName()))
                .toList()
        );
    }
}
