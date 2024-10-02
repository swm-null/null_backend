package com.example.oatnote._config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.context.annotation.Configuration;

import com.example.oatnote.web.exception.OatException;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableAsync
@Slf4j
public class AsyncConfig implements AsyncConfigurer {

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, params) -> {
            if (throwable instanceof OatException ex) {
                log.error("비동기 작업 중 예외 발생 - {} - {}", method.getName(), ex.getDetail());
            } else {
                // 일반 예외 처리 로직
                log.error("비동기 작업 알 수 없는 예외 발생 - {} - {}", method.getName(), throwable.getMessage());
            }
        };
    }
}

