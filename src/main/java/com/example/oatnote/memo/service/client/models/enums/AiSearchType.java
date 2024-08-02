package com.example.oatnote.memo.service.client.models.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum AiSearchType {
    SIMILARITY("similarity"),
    REGEX("regex"),
    TAG("tag"),
    UNSPECIFIED("unspecified");

    private final String value;

    AiSearchType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static AiSearchType forValue(String value) {
        return Arrays.stream(AiSearchType.values())
            .filter(type -> type.value.equals(value))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unknown enum type " + value));
    }
}
