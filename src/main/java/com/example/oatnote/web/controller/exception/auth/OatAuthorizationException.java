package com.example.oatnote.web.controller.exception.auth;

import com.example.oatnote.web.controller.enums.ErrorEnum;
import com.example.oatnote.web.controller.exception.OatException;

public class OatAuthorizationException extends OatException {

    private static final ErrorEnum ERROR_RESPONSE = ErrorEnum.AUTHORIZATION_EXCEPTION;

    protected OatAuthorizationException(ErrorEnum errorEnum, String detail) {
        super(errorEnum, detail);
    }

    public static OatAuthorizationException withDetail(String detail) {
        return new OatAuthorizationException(ERROR_RESPONSE, detail);
    }

    public static OatAuthorizationException withDetail(String detail, String id) {
        return withDetail(String.format("%s : %s", detail, id));
    }
}
