package com.example.oatnote.memo.service.client.models;

import java.util.List;

public record AiCreateTagResponse(
    List<Double> embedding
) {

}
