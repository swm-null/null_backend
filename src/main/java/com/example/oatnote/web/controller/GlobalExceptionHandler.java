package com.example.oatnote.web.controller;

import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.oatnote.memotag.service.memo.exception.MemoNotFoundException;
import com.example.oatnote.memotag.service.tag.exception.TagNotFoundException;
import com.example.oatnote.user.service.exception.UserAuthException;
import com.example.oatnote.user.service.exception.UserNotFoundException;
import com.example.oatnote.user.service.email.exception.EmailDispatchException;
import com.example.oatnote.user.service.email.exception.EmailVerificationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthIllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleAuthIllegalArgumentException(AuthIllegalArgumentException ex) {
        return buildErrorResponse(1001, ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAuthException.class)
    public ResponseEntity<ErrorResponse> handleUserIllegalArgumentException(UserAuthException ex) {
        return buildErrorResponse(1002, ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        return buildErrorResponse(1003, ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MemoNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMemoNotFoundException(MemoNotFoundException ex) {
        return buildErrorResponse(1004, ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TagNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTagNotFoundException(TagNotFoundException ex) {
        return buildErrorResponse(1005, ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException ex
    ) {
        String errorMessages = ex.getBindingResult().getAllErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.joining(", "));
        return buildErrorResponse(1007, errorMessages, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        return buildErrorResponse(1008, ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailDispatchException.class)
    public ResponseEntity<ErrorResponse> handleEmailDispatchException(EmailDispatchException ex) {
        return buildErrorResponse(1009, ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailVerificationException.class)
    public ResponseEntity<ErrorResponse> handleEmailVerificationException(EmailVerificationException ex) {
        return buildErrorResponse(1010, ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(int errorCode, String message, HttpStatus status) {
        ErrorResponse errorResponse = new ErrorResponse(errorCode, message);
        return new ResponseEntity<>(errorResponse, status);
    }
}
