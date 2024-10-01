package com.example.oatnote.web.exception;

import com.example.oatnote.web.controller.enums.ErrorEnum;

public class OatExternalServiceException extends OatException{

    private static final ErrorEnum ERROR_RESPONSE = ErrorEnum.EXTERNAL_SERVICE_ERROR;

    public OatExternalServiceException(String detail) {
        super(ERROR_RESPONSE, detail);
    }

    public static OatExternalServiceException withDetail(String detail) {
        return new OatExternalServiceException(detail);
    }
}
