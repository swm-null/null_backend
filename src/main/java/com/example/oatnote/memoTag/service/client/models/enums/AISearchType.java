package com.example.oatnote.memoTag.service.client.models.enums;

public enum AISearchType {
    SIMILARITY("similarity"),
    REGEX("regex"),
    TAG("tag"),
    ;

    private final String value;

    AISearchType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
