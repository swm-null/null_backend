package com.example.oatnote.web.exception;

public class OatIllegalArgumentException extends OatException {

    private static final String DEFAULT_MESSAGE = "잘못된 요청입니다.";

    public OatIllegalArgumentException(String message) {
        super(message);
    }

    public OatIllegalArgumentException(String message, String detail) {
        super(message, detail);
    }

    public static OatIllegalArgumentException withDetail(String detail) {
        return new OatIllegalArgumentException(DEFAULT_MESSAGE, detail);
    }
}
