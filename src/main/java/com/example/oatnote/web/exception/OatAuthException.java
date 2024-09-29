package com.example.oatnote.web.exception;

public class OatAuthException extends OatException {

    private static final String DEFAULT_MESSAGE = "인증에 실패했습니다.";

    public OatAuthException(String message) {
        super(message);
    }

    public OatAuthException(String message, String detail) {
        super(message, detail);
    }

    public static OatAuthException withDetail(String detail) {
        return new OatAuthException(DEFAULT_MESSAGE, detail);
    }
}
