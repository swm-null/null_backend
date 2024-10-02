package com.example.oatnote.web.exception.auth;

import com.example.oatnote.web.controller.enums.ErrorEnum;

public class OatExpiredAccessTokenException extends OatAuthorizationException {

    private static final ErrorEnum ERROR_RESPONSE = ErrorEnum.EXPIRED_ACCESS_TOKEN;

    public OatExpiredAccessTokenException(String detail) {
        super(ERROR_RESPONSE, detail);
    }

    public static OatExpiredAccessTokenException withDetail(String detail) {
        return new OatExpiredAccessTokenException(detail);
    }
}