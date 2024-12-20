package com.example.oatnote.web.controller.exception.auth;

import com.example.oatnote.web.controller.enums.ErrorEnum;
import com.example.oatnote.web.controller.exception.OatException;

public class OatInvalidPasswordException extends OatException {

    private static final ErrorEnum ERROR_RESPONSE = ErrorEnum.INVALID_PASSWORD;

    public OatInvalidPasswordException(String detail) {
        super(ERROR_RESPONSE, detail);
    }

    public static OatInvalidPasswordException withDetail(String detail) {
        return new OatInvalidPasswordException(detail);
    }

    public static OatInvalidPasswordException withDetail(String detail, String id) {
        return withDetail(String.format("%s: %s", detail, id));
    }
}
