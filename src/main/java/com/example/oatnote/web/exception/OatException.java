package com.example.oatnote.web.exception;

public abstract class OatException extends RuntimeException {

    protected final String detail;

    protected OatException(String message) {
        super(message);
        this.detail = null;
    }

    protected OatException(String message, String detail) {
        super(message);
        this.detail = detail;
    }

    protected String getFullMessage() {
        return String.format("%s %s", getMessage(), detail);
    }
}
