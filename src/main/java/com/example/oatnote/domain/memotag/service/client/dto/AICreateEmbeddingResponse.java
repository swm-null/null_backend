package com.example.oatnote.domain.memotag.service.client.dto;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotEmpty;

@JsonNaming(SnakeCaseStrategy.class)
public record AICreateEmbeddingResponse(
    @NotEmpty(message = "임베딩값은 비워둘 수 없습니다.")
    List<Double> embedding
) {

}
