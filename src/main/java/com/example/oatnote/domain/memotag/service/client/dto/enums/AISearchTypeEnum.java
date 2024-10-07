package com.example.oatnote.domain.memotag.service.client.dto.enums;

public enum AISearchTypeEnum {
    SIMILARITY("similarity"),
    REGEX("regex"),
    ;

    private final String value;

    AISearchTypeEnum(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
