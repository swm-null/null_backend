package com.example.memo.memo.service.exception;

public class MemoNotFoundException extends RuntimeException {

    public MemoNotFoundException(String message) {
        super(message);
    }
}
