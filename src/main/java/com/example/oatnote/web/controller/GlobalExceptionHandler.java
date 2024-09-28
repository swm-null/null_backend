package com.example.oatnote.web.controller;

import java.time.DateTimeException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.catalina.connector.ClientAbortException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

import com.example.oatnote.web.exception.OatAuthException;
import com.example.oatnote.web.exception.OatDataNotFoundException;
import com.example.oatnote.web.exception.OatException;
import com.example.oatnote.web.exception.OatExternalServiceException;
import com.example.oatnote.web.exception.OatIllegalArgumentException;
import com.example.oatnote.web.exception.OatIllegalStateException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(OatException.class)
    public ResponseEntity<Object> handleOatException(OatException ex) {
        return buildErrorResponse(1000, ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(OatIllegalStateException.class)
    public ResponseEntity<Object> handleOatIllegalStateException(OatIllegalStateException ex) {
        return buildErrorResponse(1001, ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(OatIllegalArgumentException.class)
    public ResponseEntity<Object> handleOatIllegalArgumentException(OatIllegalArgumentException ex) {
        return buildErrorResponse(1002, ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OatDataNotFoundException.class)
    public ResponseEntity<Object> handleOatDataNotFoundException(OatDataNotFoundException ex) {
        return buildErrorResponse(1003, ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OatAuthException.class)
    public ResponseEntity<Object> handleOatAuthException(OatAuthException ex) {
        return buildErrorResponse(1004, ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(OatExternalServiceException.class)
    public ResponseEntity<Object> handleOatExternalServiceException(OatExternalServiceException ex) {
        return buildErrorResponse(1005, ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 표준 예외 처리

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(
        HttpServletRequest request,
        Exception e
    ) {
        String errorMessage = e.getMessage();
        String errorFile = e.getStackTrace()[0].getFileName();
        int errorLine = e.getStackTrace()[0].getLineNumber();
        String errorName = e.getClass().getSimpleName();
        String detail = String.format("""
                Exception: *%s*
                Location: *%s Line %d*
                
                ```%s```
                """,
            errorName, errorFile, errorLine, errorMessage);
        log.error("""
            서버에서 에러가 발생했습니다. uri: {} {}
            {}
            """, request.getMethod(), request.getRequestURI(), detail);
        requestLogging(request);
        return buildErrorResponse(2000, "서버에서 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(
        HttpServletRequest request,
        IllegalArgumentException e
    ) {
        log.warn(e.getMessage());
        requestLogging(request);
        return buildErrorResponse(2001, e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handleIllegalStateException(
        HttpServletRequest request,
        IllegalStateException e
    ) {
        log.warn(e.getMessage());
        requestLogging(request);
        return buildErrorResponse(2002, e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DateTimeException.class)
    public ResponseEntity<Object> handleDateTimeParseException(
        HttpServletRequest request,
        DateTimeException e
    ) {
        log.warn(e.getMessage());
        requestLogging(request);
        return buildErrorResponse(2003, "잘못된 날짜 형식입니다.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<Object> handleUnsupportedOperationException(
        HttpServletRequest request,
        UnsupportedOperationException e
    ) {
        log.warn(e.getMessage());
        requestLogging(request);
        return buildErrorResponse(2004, "지원하지 않는 API 입니다.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ClientAbortException.class)
    public ResponseEntity<Object> handleClientAbortException(
        HttpServletRequest request,
        ClientAbortException e
    ) {
        log.warn("클라이언트가 연결을 중단했습니다: {}", e.getMessage());
        requestLogging(request);
        return buildErrorResponse(2005, "클라이언트에 의해 연결이 중단되었습니다", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        @NonNull MethodArgumentNotValidException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode status,
        @NonNull WebRequest webRequest
    ) {
        HttpServletRequest request = ((ServletWebRequest)webRequest).getRequest();
        log.warn("검증 과정에서 문제가 발생했습니다. uri: {} {}, ", request.getMethod(), request.getRequestURI(), ex);
        requestLogging(request);
        String errorMessages = ex.getBindingResult().getAllErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.joining(", "));
        return buildErrorResponse(2006, errorMessages, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> buildErrorResponse(int errorCode, String message, HttpStatus status) {
        ErrorResponse errorResponse = new ErrorResponse(errorCode, message);
        return new ResponseEntity<>(errorResponse, status);
    }

    private void requestLogging(HttpServletRequest request) {
        log.info("request header: {}", getHeaders(request));
        log.info("request query string: {}", getQueryString(request));
        log.info("request body: {}", getRequestBody(request));
    }

    private Map<String, Object> getHeaders(HttpServletRequest request) {
        Map<String, Object> headerMap = new HashMap<>();
        Enumeration<String> headerArray = request.getHeaderNames();
        while (headerArray.hasMoreElements()) {
            String headerName = headerArray.nextElement();
            headerMap.put(headerName, request.getHeader(headerName));
        }
        return headerMap;
    }

    private String getQueryString(HttpServletRequest httpRequest) {
        String queryString = httpRequest.getQueryString();
        if (queryString == null) {
            return " - ";
        }
        return queryString;
    }

    private String getRequestBody(HttpServletRequest request) {
        var wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper == null) {
            return " - ";
        }
        try {
            // body 가 읽히지 않고 예외처리 되는 경우에 캐시하기 위함
            wrapper.getInputStream().readAllBytes();
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length == 0) {
                return " - ";
            }
            return new String(buf, wrapper.getCharacterEncoding());
        } catch (Exception e) {
            return " - ";
        }
    }
}
