package com.example.oatnote.web.controller.exception.server;

import com.example.oatnote.web.controller.enums.ErrorEnum;
import com.example.oatnote.web.controller.exception.OatException;

public class OatIllegalStateException extends OatException {

    private static final ErrorEnum ERROR_RESPONSE = ErrorEnum.ILLEGAL_STATE;

    public OatIllegalStateException(String detail) {
        super(ERROR_RESPONSE, detail);
    }

    public static OatIllegalStateException withDetail(String detail) {
        return new OatIllegalStateException(detail);
    }

    public static OatIllegalStateException withDetail(String detail, String id) {
        return withDetail(String.format("%s: %s", detail, id));
    }
}
