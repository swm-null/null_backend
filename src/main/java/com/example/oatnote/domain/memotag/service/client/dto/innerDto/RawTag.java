package com.example.oatnote.domain.memotag.service.client.dto.innerDto;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record RawTag(
    String id,
    String name,
    boolean isNew
) {

}
