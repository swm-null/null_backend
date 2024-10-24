package com.example.oatnote.domain.memotag.dto;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@JsonNaming(SnakeCaseStrategy.class)
public record UpdateTagRequest(
    @Schema(description = "태그 이름", example = "일정")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣_\\s]*$", message = "영어, 숫자, 한글, 언더바 및 공백만 허용됩니다.")
    @Size(max = 20, message = "태그 이름은 최대 10자까지 입력 가능합니다.")
    String name
) {

}
