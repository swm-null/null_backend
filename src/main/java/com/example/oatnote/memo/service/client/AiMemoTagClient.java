package com.example.oatnote.memo.service.client;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.oatnote.memo.service.client.models.AiCreateMemoRequest;
import com.example.oatnote.memo.service.client.models.AiCreateMemoResponse;
import com.example.oatnote.memo.service.client.models.AiCreateTagRequest;
import com.example.oatnote.memo.service.client.models.AiCreateTagResponse;
import com.example.oatnote.memo.service.client.models.AiSearchMemoRequest;
import com.example.oatnote.memo.service.client.models.AiSearchMemoResponse;

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

    public AiCreateMemoResponse createMemo(String content) {
        final URI uri = UriComponentsBuilder
            .fromUriString(aiUrl)
            .path("/add_memo/")
            .encode()
            .build()
            .toUri();
        AiCreateMemoRequest aiCreateMemoRequest = new AiCreateMemoRequest(content, null); //Todo timestamp
        ResponseEntity<AiCreateMemoResponse> aiCreateMemoResponse = restTemplate.postForEntity(
            uri,
            aiCreateMemoRequest,
            AiCreateMemoResponse.class
        );
        return aiCreateMemoResponse.getBody();
    }

    public AiSearchMemoResponse searchMemo(String content) {
        final URI uri = UriComponentsBuilder
            .fromUriString(aiUrl)
            .path("/search/")
            .encode()
            .build()
            .toUri();
        AiSearchMemoRequest aiSearchMemoRequest = new AiSearchMemoRequest(content);
        ResponseEntity<AiSearchMemoResponse> aiResponse = restTemplate.postForEntity(
            uri,
            aiSearchMemoRequest,
            AiSearchMemoResponse.class
        );
        return aiResponse.getBody();
    }

    public AiCreateTagResponse createTag(String name) {
        final URI uri = UriComponentsBuilder
            .fromUriString(aiUrl)
            .path("/get_embedding/")
            .encode()
            .build()
            .toUri();
        AiCreateTagRequest aiCreateTagRequest = new AiCreateTagRequest(name);
        ResponseEntity<AiCreateTagResponse> aiCreateTagResponse = restTemplate.postForEntity(
            uri,
            aiCreateTagRequest,
            AiCreateTagResponse.class
        );
        return aiCreateTagResponse.getBody();
    }
}
