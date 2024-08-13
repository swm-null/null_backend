package com.example.oatnote.memo.models;

import java.util.List;

import com.example.oatnote.memo.service.tag.models.Tag;

import io.swagger.v3.oas.annotations.media.Schema;

public record CreateTagRequest(
    @Schema(description = "태그 이름", example = "일정")
    String name
) {

    public Tag toTag(List<Double> embedding) {
        return Tag.builder()
            .name(name)
            .embedding(embedding)
            .build();
    }
}
