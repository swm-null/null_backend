package com.example.memo._config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component
public class Config {

    @Value("${MONGODB_URI}")
    private String mongodbUri;

    @Value("${AI_URL}")
    private String aiUrl;
}
