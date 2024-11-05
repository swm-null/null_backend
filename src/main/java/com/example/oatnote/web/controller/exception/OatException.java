package com.example.oatnote.web.controller.exception;

import com.example.oatnote.web.controller.enums.ErrorEnum;

import lombok.Getter;

@Getter
public abstract class OatException extends RuntimeException {

    private final ErrorEnum errorEnum;
    protected final String detail;

    protected OatException(ErrorEnum errorEnum, String detail) {
        super(errorEnum.getMessage());
        this.errorEnum = errorEnum;
        this.detail = detail;
    }

    public String getFullMessage() {
        return String.format("[%s] %s %s", errorEnum.getCode(), errorEnum.getMessage(), detail);
    }
}
