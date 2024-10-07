package com.example.oatnote.domain.memotag.dto.enums;

public enum SortOrderTypeEnum {
    LATEST("latest"),
    OLDEST("oldest"),
    NAME("name"),
    ;

    private final String value;

    SortOrderTypeEnum(String value) {
        this.value = value.toUpperCase();
    }

    @Override
    public String toString() {
        return value;
    }
}
