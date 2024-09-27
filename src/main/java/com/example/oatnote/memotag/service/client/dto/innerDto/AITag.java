package com.example.oatnote.memotag.service.client.dto.innerDto;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record AITag(
    String id,
    String name,
    boolean isNew
) {

}
