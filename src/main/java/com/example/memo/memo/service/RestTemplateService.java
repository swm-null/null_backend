package com.example.memo.memo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.memo.memo.service.models.AiSaveResponse;
import com.example.memo.memo.service.models.AiSearchResponse;
import com.example.memo.memo.service.models.MemoRequestBridge;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestTemplateService {

    private final RestTemplate restTemplate;

    @Value("${AI_URL}")
    private String aiUrl;

    public AiSaveResponse getTags(MemoRequestBridge memoRequestBridge) {
        final String uri = aiUrl + "/add_memo/";
        ResponseEntity<AiSaveResponse> aiResponse = restTemplate.postForEntity(
            uri,
            memoRequestBridge,
            AiSaveResponse.class
        );
        return aiResponse.getBody();
    }

    public AiSearchResponse searchMemo(String content) {
        final String uri = aiUrl + "/user_query?query=" + content;
        ResponseEntity<AiSearchResponse> aiResponse = restTemplate.getForEntity(uri, AiSearchResponse.class);
        return aiResponse.getBody();
    }
}
