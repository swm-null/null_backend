package com.example.oatnote.web.exception;

public class OatDataNotFoundException extends OatException {

    private static final String DEFAULT_MESSAGE = "데이터를 찾을 수 없습니다.";

    public OatDataNotFoundException(String message) {
        super(message);
    }

    public OatDataNotFoundException(String message, String detail) {
        super(message, detail);
    }

    public static OatDataNotFoundException withDetail(String detail) {
        return new OatDataNotFoundException(DEFAULT_MESSAGE, detail);
    }
}
