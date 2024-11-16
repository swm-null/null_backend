package com.example.oatnote.web.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.catalina.connector.ClientAbortException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
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

import com.example.oatnote.web.controller.dto.ErrorResponse;
import com.example.oatnote.web.controller.enums.ErrorEnum;
import com.example.oatnote.web.controller.exception.auth.OatAuthorizationException;
import com.example.oatnote.web.controller.exception.client.OatDataNotFoundException;
import com.example.oatnote.web.controller.exception.OatException;
import com.example.oatnote.web.controller.exception.server.OatExternalServiceException;
import com.example.oatnote.web.controller.exception.client.OatIllegalArgumentException;
import com.example.oatnote.web.controller.exception.server.OatIllegalStateException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // 커스텀 예외 처리
    @ExceptionHandler(OatException.class)
    public ResponseEntity<Object> handleOatException(OatException e) {
        log.warn(e.getFullMessage());
        return buildErrorResponse(e.getErrorEnum(), e.getDetail());
    }

    @ExceptionHandler(OatIllegalArgumentException.class)
    public ResponseEntity<Object> handleOatIllegalArgumentException(OatIllegalArgumentException e) {
        log.warn(e.getFullMessage());
        return buildErrorResponse(e.getErrorEnum(), e.getDetail());
    }

    @ExceptionHandler(OatIllegalStateException.class)
    public ResponseEntity<Object> handleOatIllegalStateException(OatIllegalStateException e) {
        log.warn(e.getFullMessage());
        return buildErrorResponse(e.getErrorEnum(), e.getDetail());
    }

    @ExceptionHandler(OatAuthorizationException.class)
    public ResponseEntity<Object> handleOatAuthException(OatAuthorizationException e) {
        log.warn(e.getFullMessage());
        return buildErrorResponse(e.getErrorEnum(), e.getDetail());
    }

    @ExceptionHandler(OatDataNotFoundException.class)
    public ResponseEntity<Object> handleOatDataNotFoundException(OatDataNotFoundException e) {
        log.warn(e.getFullMessage());
        return buildErrorResponse(e.getErrorEnum(), e.getDetail());
    }

    @ExceptionHandler(OatExternalServiceException.class)
    public ResponseEntity<Object> handleOatExternalServiceException(OatExternalServiceException e) {
        log.warn(e.getFullMessage());
        return buildErrorResponse(e.getErrorEnum(), e.getDetail());
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
        return buildErrorResponse(ErrorEnum.GENERIC_SERVER_ERROR, null);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(
        HttpServletRequest request,
        IllegalArgumentException e
    ) {
        log.warn(e.getMessage());
        requestLogging(request);
        return buildErrorResponse(ErrorEnum.ILLEGAL_ARGUMENT, null);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handleIllegalStateException(
        HttpServletRequest request,
        IllegalStateException e
    ) {
        log.warn(e.getMessage());
        requestLogging(request);
        return buildErrorResponse(ErrorEnum.ILLEGAL_ARGUMENT, null);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        @NonNull MethodArgumentNotValidException e,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode status,
        @NonNull WebRequest webRequest
    ) {
        HttpServletRequest request = ((ServletWebRequest)webRequest).getRequest();
        String detail = e.getBindingResult().getAllErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.joining(", "));
        log.warn("검증 과정에서 문제가 발생했습니다. uri: {}, {}, {}, ", request.getMethod(), request.getRequestURI(), detail);
        requestLogging(request);
        return buildErrorResponse(ErrorEnum.VALIDATION_ERROR, detail);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(
        HttpServletRequest request,
        ConstraintViolationException e
    ) {
        String detail = e.getConstraintViolations().stream()
            .map(ConstraintViolation::getMessage)
            .collect(Collectors.joining(", "));
        log.warn("유효성 검사 실패. uri: {}, {}, {}", request.getMethod(), request.getRequestURI(), detail);
        requestLogging(request);
        return buildErrorResponse(ErrorEnum.VALIDATION_ERROR, detail);
    }

    @ExceptionHandler(ClientAbortException.class)
    public ResponseEntity<Object> handleClientAbortException(HttpServletRequest request, ClientAbortException e) {
        log.warn("클라이언트가 연결을 중단했습니다: {}", e.getMessage());
        requestLogging(request);
        return buildErrorResponse(ErrorEnum.CLIENT_ABORT, null);
    }

    // 공통 메서드
    private ResponseEntity<Object> buildErrorResponse(ErrorEnum errorEnum, String detail) {
        ErrorResponse errorResponse = ErrorResponse.from(errorEnum, detail);
        return new ResponseEntity<>(errorResponse, errorEnum.getStatus());
    }

    private void requestLogging(HttpServletRequest request) {
        log.info("Request headers: {}", getHeaders(request));
        log.info("Request query string: {}", getQueryString(request));
        log.info("Request body: {}", getRequestBody(request));
    }

    private Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
        return headers;
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
        } catch (Exception e
        ) {
            return " - ";
        }
    }
}
