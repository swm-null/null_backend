package com.example.oatnote.user.service.email.exception;

public class EmailVerificationException extends IllegalArgumentException{

    public EmailVerificationException(String message) {
        super(message);
    }
}
