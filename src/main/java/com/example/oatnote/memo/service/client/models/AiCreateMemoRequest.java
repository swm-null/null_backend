package com.example.oatnote.memo.service.client.models;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@JsonNaming(SnakeCaseStrategy.class)
public record AiCreateMemoRequest(
    @NotBlank(message = "내용은 비워둘 수 없습니다.")
    String content,

    LocalDateTime timestamp
) {

    public static AiCreateMemoRequest from(String content) {
        return new AiCreateMemoRequest(content, null);
    }
}
