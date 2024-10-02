package com.example.oatnote.web.controller.dto;

import com.example.oatnote.web.controller.enums.ErrorEnum;

public record ErrorResponse(
    String code,
    String message,
    String detail
) {

    public static ErrorResponse from(ErrorEnum errorEnum, String detail) {
        return new ErrorResponse(errorEnum.getCode(), errorEnum.getMessage(), detail);
    }
}
