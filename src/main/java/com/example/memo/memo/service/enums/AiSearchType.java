package com.example.memo.memo.service.enums;

import lombok.Getter;

@Getter
public enum AiSearchType {
    SIMILARITY("similarity"),
    REGEX("regex"),
    TAG("tag"),
    ;

    private final String type;

    AiSearchType(String type) {
        this.type = type.toLowerCase();
    }
}
