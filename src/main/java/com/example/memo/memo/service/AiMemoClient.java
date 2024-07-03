package com.example.memo.memo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.memo.memo.service.models.AiSaveResponse;
import com.example.memo.memo.service.models.AiSearchResponse;
import com.example.memo.memo.service.models.MemoRequestBridge;

@Service
public class AiMemoClient {

    private final RestTemplate restTemplate;
    private final String aiUrl;

    public AiMemoClient(RestTemplate restTemplate, @Value("${spring.ai.url}") String aiUrl) {
        this.restTemplate = restTemplate;
        this.aiUrl = aiUrl;
    }

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
