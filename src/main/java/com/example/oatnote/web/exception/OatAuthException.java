package com.example.oatnote.web.exception;

import com.example.oatnote.web.controller.enums.ErrorEnum;

public class OatAuthException extends OatException {

    private static final ErrorEnum ERROR_RESPONSE = ErrorEnum.AUTH_EXCEPTION;

    public OatAuthException(String detail) {
        super(ERROR_RESPONSE, detail);
    }

    public static OatAuthException withDetail(String detail) {
        return new OatAuthException(detail);
    }
}
