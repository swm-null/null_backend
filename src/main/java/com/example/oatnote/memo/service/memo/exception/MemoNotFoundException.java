package com.example.oatnote.memo.service.memo.exception;

public class MemoNotFoundException extends RuntimeException {

    public MemoNotFoundException(String message) {
        super(message);
    }
}
