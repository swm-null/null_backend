package com.example.oatnote.memotag.service.memo.exception;

public class MemoNotFoundException extends RuntimeException {

    public MemoNotFoundException(String message) {
        super(message);
    }
}
