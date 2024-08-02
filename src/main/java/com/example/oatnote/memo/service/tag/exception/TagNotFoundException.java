package com.example.oatnote.memo.service.tag.exception;

public class TagNotFoundException extends RuntimeException {

    public TagNotFoundException(String message) {
        super(message);
    }
}
