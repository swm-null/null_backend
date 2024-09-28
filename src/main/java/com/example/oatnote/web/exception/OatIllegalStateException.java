package com.example.oatnote.web.exception;

public class OatIllegalStateException extends OatException {

    private static final String DEFAULT_MESSAGE = "잘못된 상태입니다.";

    public OatIllegalStateException(String message) {
        super(message);
    }

    public OatIllegalStateException(String message, String detail) {
        super(message, detail);
    }

    public static OatIllegalStateException withDetail(String detail) {
        return new OatIllegalStateException(DEFAULT_MESSAGE, detail);
    }
}
