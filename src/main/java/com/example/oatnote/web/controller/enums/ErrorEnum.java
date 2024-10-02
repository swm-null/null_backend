package com.example.oatnote.web.controller.enums;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorEnum {
    // 인증 에러
    AUTHORIZATION_EXCEPTION("0000", "인증에 실패했습니다.", HttpStatus.UNAUTHORIZED),
    INVALID_PASSWORD("0001", "비밀번호가 잘못되었습니다.", HttpStatus.UNAUTHORIZED),
    MISSING_TOKEN("0002", "토큰이 없습니다.", HttpStatus.UNAUTHORIZED),
    EXPIRED_ACCESS_TOKEN("0003", "액세스 토큰이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
    EXPIRED_REFRESH_TOKEN("0004", "리프레시 토큰이 만료되었습니다.", HttpStatus.UNAUTHORIZED),

    // 클라이언트 에러
    INVALID_ARGUMENT("1001", "잘못된 인자입니다.", HttpStatus.BAD_REQUEST),
    DATA_NOT_FOUND("1002", "데이터를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    VALIDATION_ERROR("1003", "유효성 검사가 실패했습니다.", HttpStatus.BAD_REQUEST),
    ILLEGAL_ARGUMENT("1004", "잘못된 인자입니다.", HttpStatus.BAD_REQUEST),

    // 서버 에러
    INTERNAL_SERVER_ERROR("2000", "서버 내부 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    ILLEGAL_STATE("2001", "잘못된 상태입니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    EXTERNAL_SERVICE_ERROR("2002", "외부 서비스에서 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    GENERIC_SERVER_ERROR("2003", "서버에서 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    CLIENT_ABORT("2004", "클라이언트가 연결을 중단했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    ;

    private final String code;
    private final String message;
    private final HttpStatus status;
}
