package com.example.oatnote.web.exception;

import com.example.oatnote.web.controller.enums.ErrorEnum;

public class OatIllegalStateException extends OatException {

    private static final ErrorEnum ERROR_RESPONSE = ErrorEnum.ILLEGAL_STATE;

    public OatIllegalStateException(String detail) {
        super(ERROR_RESPONSE, detail);
    }

    public static OatIllegalStateException withDetail(String detail) {
        return new OatIllegalStateException(detail);
    }
}
