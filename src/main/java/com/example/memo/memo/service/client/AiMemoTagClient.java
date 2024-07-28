package com.example.memo.memo.service.client;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.memo.memo.service.client.models.AiCreateRequest;
import com.example.memo.memo.service.client.models.AiCreateResponse;
import com.example.memo.memo.service.client.models.AiSearchRequest;
import com.example.memo.memo.service.client.models.AiSearchResponse;

@Service
public class AiMemoTagClient {

    private final RestTemplate restTemplate;
    private final String aiUrl;

    public AiMemoTagClient(
        RestTemplate restTemplate,
        @Value("${spring.ai.url}") String aiUrl
    ) {
        this.restTemplate = restTemplate;
        this.aiUrl = aiUrl;
    }

    public AiCreateResponse createMemo(String content) {
        final URI uri = UriComponentsBuilder
            .fromUriString(aiUrl)
            .path("/add_memo/")
            .encode()
            .build()
            .toUri();
        AiCreateRequest aiCreateRequest = new AiCreateRequest(content);
        ResponseEntity<AiCreateResponse> aiResponse = restTemplate.postForEntity(
            uri,
            aiCreateRequest,
            AiCreateResponse.class
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