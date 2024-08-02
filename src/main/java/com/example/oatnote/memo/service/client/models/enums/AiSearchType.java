package com.example.oatnote.memo.service.client.models.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum AiSearchType {
    SIMILARITY("similarity"),
    REGEX("regex"),
    TAG("tag"),
    ;

    private final String value;

    AiSearchType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
