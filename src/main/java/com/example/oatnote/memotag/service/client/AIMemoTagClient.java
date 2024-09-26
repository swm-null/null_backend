package com.example.oatnote.memotag.service.client;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.oatnote.memotag.service.client.dto.AICreateEmbeddingRequest;
import com.example.oatnote.memotag.service.client.dto.AICreateEmbeddingResponse;
import com.example.oatnote.memotag.service.client.dto.AICreateTagsRequest;
import com.example.oatnote.memotag.service.client.dto.AICreateTagsResponse;
import com.example.oatnote.memotag.service.client.dto.AICreateMemosRequest;
import com.example.oatnote.memotag.service.client.dto.AICreateMemosResponse;
import com.example.oatnote.memotag.service.client.dto.AISearchMemosRequest;
import com.example.oatnote.memotag.service.client.dto.AISearchMemosResponse;

@Service
public class AIMemoTagClient {

    private final RestTemplate restTemplate;
    private final String aiUrl;

    public AIMemoTagClient(
        RestTemplate restTemplate,
        @Value("${spring.ai.url}") String aiUrl
    ) {
        this.restTemplate = restTemplate;
        this.aiUrl = aiUrl;
    }

    public AICreateTagsResponse createTags(AICreateTagsRequest aiCreateTagsRequest) {
        final URI uri = buildUri("/memo/tag");
        ResponseEntity<AICreateTagsResponse> aiCreateTagsResponse = restTemplate.postForEntity(
            uri,
            aiCreateTagsRequest,
            AICreateTagsResponse.class
        );
        return aiCreateTagsResponse.getBody();
    }

    public AICreateMemosResponse createMemosTags(String content, String userId) {
        final URI uri = buildUri("/kakao-parser");
        final String type = content.substring(content.length() - 3);
        AICreateMemosRequest aiCreateMemosRequest = AICreateMemosRequest.from(content, type, userId);
        ResponseEntity<AICreateMemosResponse> aiCreateKakaoMemosResponse = restTemplate.postForEntity(
            uri,
            aiCreateMemosRequest,
            AICreateMemosResponse.class
        );
        return aiCreateKakaoMemosResponse.getBody();
    }

    public AISearchMemosResponse searchMemo(AISearchMemosRequest aiSearchMemosRequest) {
        final URI uri = buildUri("/search");
        ResponseEntity<AISearchMemosResponse> aiSearchMemosResponse = restTemplate.postForEntity(
            uri,
            aiSearchMemosRequest,
            AISearchMemosResponse.class
        );
        return aiSearchMemosResponse.getBody();
    }

    public AICreateEmbeddingResponse createEmbedding(String name) {
        final URI uri = buildUri("/get-embedding");
        AICreateEmbeddingRequest aiCreateEmbeddingRequest = AICreateEmbeddingRequest.from(name);
        ResponseEntity<AICreateEmbeddingResponse> aiCreateEmbeddingResponse = restTemplate.postForEntity(
            uri,
            aiCreateEmbeddingRequest,
            AICreateEmbeddingResponse.class
        );
        return aiCreateEmbeddingResponse.getBody();
    }

    private URI buildUri(String path) {
        return UriComponentsBuilder
            .fromUriString(aiUrl)
            .path(path)
            .encode()
            .build()
            .toUri();
    }
}
