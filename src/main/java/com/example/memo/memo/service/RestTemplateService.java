package com.example.memo.memo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.memo.memo.service.exception.AiResponseErrorHandler;
import com.example.memo.memo.service.models.AiSearchResponse;
import com.example.memo.memo.service.models.MemoRequestBridge;
import com.example.memo.memo.service.models.AiSaveResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestTemplateService {

    private final RestTemplate restTemplate;

    @Value("${AI_URL}")
    private String aiUrl;

    public AiSaveResponse getTags(MemoRequestBridge memoRequestBridge) {
        restTemplate.setErrorHandler(new AiResponseErrorHandler());
        final String query = "/add_memo/";
        final String uri = String.format("%s%s", aiUrl, query);
        ResponseEntity<AiSaveResponse> aiResponse = restTemplate.postForEntity(uri, memoRequestBridge,
            AiSaveResponse.class);
        return aiResponse.getBody();
    }

    public AiSearchResponse searchMemo(String content) {
        restTemplate.setErrorHandler(new AiResponseErrorHandler());
        final String query = "/user_query?query=";
        final String uri = String.format("%s%s%s", aiUrl, query, content);
        ResponseEntity<AiSearchResponse> aiResponse = restTemplate.getForEntity(uri, AiSearchResponse.class);
        return aiResponse.getBody();
    }
}
