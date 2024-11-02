package com.example.oatnote.domain.memotag.service.client.dto.enums;

public enum AiSearchTypeEnum {
    SIMILARITY("similarity"),
    REGEX("regex"),
    ;

    private final String value;

    AiSearchTypeEnum(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
