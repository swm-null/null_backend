package com.example.oatnote._config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.example.oatnote.memoTag.service.client.exception.AiResponseErrorHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class RestTemplateConfig {

    private final ObjectMapper objectMapper;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(0, new MappingJackson2HttpMessageConverter(objectMapper));
        restTemplate.setErrorHandler(new AiResponseErrorHandler());
        return restTemplate;
    }
}
