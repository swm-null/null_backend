package com.example.oatnote.memoTag.service.client.models.enums;

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
