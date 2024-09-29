package com.example.oatnote.web.exception;

public class OatExternalServiceException extends OatException{

    private static final String DEFAULT_MESSAGE = "외부 서비스 호출에 실패했습니다.";

    public OatExternalServiceException(String message) {
        super(message);
    }

    public OatExternalServiceException(String message, String detail) {
        super(message, detail);
    }

    public static OatExternalServiceException withDetail(String detail) {
        return new OatExternalServiceException(DEFAULT_MESSAGE, detail);
    }
}
