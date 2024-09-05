package com.example.oatnote.memoTag.dto;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

@JsonNaming(SnakeCaseStrategy.class)
public record CreateTagRequest(
    @Schema(description = "태그 이름", example = "일정")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣_\\s]*$", message = "영어, 숫자, 한글, 언더바 및 공백만 허용됩니다.")
    String name
) {

}
