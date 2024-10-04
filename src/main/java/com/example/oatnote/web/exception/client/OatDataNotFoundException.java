package com.example.oatnote.web.exception.client;

import com.example.oatnote.web.controller.enums.ErrorEnum;
import com.example.oatnote.web.exception.OatException;

public class OatDataNotFoundException extends OatException {

    private static final ErrorEnum ERROR_RESPONSE = ErrorEnum.DATA_NOT_FOUND;

    public OatDataNotFoundException(String detail) {
        super(ERROR_RESPONSE, detail);
    }

    public static OatDataNotFoundException withDetail(String detail) {
        return new OatDataNotFoundException(detail);
    }

    public static OatDataNotFoundException withDetail(String detail, String id) {
        return withDetail(String.format("%s: %s", detail, id));
    }
}
