package com.example.oatnote.memotag.service.tag.exception;

public class TagNotFoundException extends RuntimeException {

    public TagNotFoundException(String message) {
        super(message);
    }
}
