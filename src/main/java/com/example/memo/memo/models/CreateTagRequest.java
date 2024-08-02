package com.example.memo.memo.models;

import java.util.LinkedList;
import java.util.List;

import com.example.memo.memo.service.models.Tag;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateTagRequest(
    @Schema(description = "태그 이름", example = "일정")
    String name
) {

    public Tag toTag(List<Double> embedding, String memoId) {
        return Tag.builder()
            .name(name)
            .embedding(embedding)
            .memoIds(new LinkedList<>(List.of(memoId)))
            .build();
    }
}
