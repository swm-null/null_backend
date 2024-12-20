package com.example.oatnote.web.controller.exception.auth;

import com.example.oatnote.web.controller.enums.ErrorEnum;

public class OatExpiredRefreshTokenException extends OatAuthorizationException {

    private static final ErrorEnum ERROR_RESPONSE = ErrorEnum.EXPIRED_REFRESH_TOKEN;

    public OatExpiredRefreshTokenException(String detail) {
        super(ERROR_RESPONSE, detail);
    }

    public static OatExpiredRefreshTokenException withDetail(String detail) {
        return new OatExpiredRefreshTokenException(detail);
    }

    public static OatExpiredRefreshTokenException withDetail(String detail, String id) {
        return withDetail(String.format("%s: %s", detail, id));
    }
}
