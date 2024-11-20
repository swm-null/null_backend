package com.example.oatnote.domain.memotag.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import java.util.List;

import com.example.oatnote.domain.memotag.service.tag.model.Tag;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

@JsonNaming(SnakeCaseStrategy.class)
public record CreateChildTagRequest(
    @Schema(description = "태그 이름", example = "일정", requiredMode = REQUIRED)
    @Pattern(regexp = "^[a-zA-Z0-9가-힣_\\s]*$", message = "태그 이름은 영어, 숫자, 한글, 언더바 및 공백만 허용됩니다.")
    String name
) {

    public Tag toTag(String userId, List<Double> embedding) {
        return new Tag(
            null,
            name,
            userId,
            embedding
        );
    }
}
