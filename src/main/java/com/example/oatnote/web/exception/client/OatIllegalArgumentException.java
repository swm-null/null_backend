package com.example.oatnote.web.exception.client;

import com.example.oatnote.web.controller.enums.ErrorEnum;
import com.example.oatnote.web.exception.OatException;

public class OatIllegalArgumentException extends OatException {

    private static final ErrorEnum ERROR_RESPONSE = ErrorEnum.ILLEGAL_ARGUMENT;

    public OatIllegalArgumentException(String detail) {
        super(ERROR_RESPONSE, detail);
    }

    public static OatIllegalArgumentException withDetail(String detail) {
        return new OatIllegalArgumentException(detail);
    }
}
