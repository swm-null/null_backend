package com.example.oatnote.domain.memotag.dto.enums;

public enum MemoSortOrderTypeEnum {
    LATEST("latest"),
    OLDEST("oldest"),
    ;

    private final String value;

    MemoSortOrderTypeEnum(String value) {
        this.value = value.toUpperCase();
    }

    @Override
    public String toString() {
        return value;
    }
}
