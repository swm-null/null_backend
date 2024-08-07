package com.example.oatnote.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.oatnote.memo.service.memo.exception.MemoNotFoundException;
import com.example.oatnote.memo.service.tag.exception.TagNotFoundException;
import com.example.oatnote.user.service.exception.AuthIllegalArgumentException;
import com.example.oatnote.user.service.exception.UserNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthIllegalArgumentException.class)
    public ResponseEntity<String> handleAuthIllegalArgumentException(AuthIllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleMemoNotFoundException(UserNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MemoNotFoundException.class)
    public ResponseEntity<String> handleMemoNotFoundException(MemoNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TagNotFoundException.class)
    public ResponseEntity<String> handleTagNotFoundException(MemoNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
