package com.example.oatnote.memo.service.client.models;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;

public record AiCreateTagResponse(
    @NotEmpty(message = "임베딩값은 비워둘 수 없습니다.")
    List<Double> embedding
) {

}
