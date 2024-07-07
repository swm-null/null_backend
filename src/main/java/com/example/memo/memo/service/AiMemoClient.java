package com.example.memo.memo.service;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.memo.memo.service.models.AiSaveRequest;
import com.example.memo.memo.service.models.AiSaveResponse;
import com.example.memo.memo.service.models.AiSearchRequest;
import com.example.memo.memo.service.models.AiSearchResponse;

@Service
public class AiMemoClient {

    private final RestTemplate restTemplate;
    private final String aiUrl;

    public AiMemoClient(
        RestTemplate restTemplate,
        @Value("${spring.ai.url}") String aiUrl
    ) {
        this.restTemplate = restTemplate;
        this.aiUrl = aiUrl;
    }

    public AiSaveResponse getTags(String content) {
        final URI uri = UriComponentsBuilder
            .fromUriString(aiUrl)
            .path("/add_memo/")
            .encode()
            .build()
            .toUri();
        AiSaveRequest aiSaveRequest = new AiSaveRequest(content);
        ResponseEntity<AiSaveResponse> aiResponse = restTemplate.postForEntity(
            uri,
            aiSaveRequest,
            AiSaveResponse.class
        );
        return aiResponse.getBody();
    }

    public AiSearchResponse searchMemo(String content) {
        final URI uri = UriComponentsBuilder
            .fromUriString(aiUrl)
            .path("/search/")
            .encode()
            .build()
            .toUri();
        AiSearchRequest aiSearchRequest = new AiSearchRequest(content);
        ResponseEntity<AiSearchResponse> aiResponse = restTemplate.postForEntity(
            uri,
            aiSearchRequest,
            AiSearchResponse.class
        );
        return aiResponse.getBody();
    }
}
