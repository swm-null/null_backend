package com.example.oatnote.memoTag.service.client;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.oatnote.memoTag.service.client.dto.AICreateEmbeddingRequest;
import com.example.oatnote.memoTag.service.client.dto.AICreateEmbeddingResponse;
import com.example.oatnote.memoTag.service.client.dto.AICreateMemoRequest;
import com.example.oatnote.memoTag.service.client.dto.AICreateMemoResponse;
import com.example.oatnote.memoTag.service.client.dto.AICreateMemosRequest;
import com.example.oatnote.memoTag.service.client.dto.AICreateMemosResponse;
import com.example.oatnote.memoTag.service.client.dto.AISearchMemoRequest;
import com.example.oatnote.memoTag.service.client.dto.AISearchMemoResponse;

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

    public AICreateMemoResponse createMemoTags(String content, String userId) {
        final URI uri = buildUri("/memo");
        AICreateMemoRequest aiCreateMemoRequest = AICreateMemoRequest.from(content, userId);
        ResponseEntity<AICreateMemoResponse> aiCreateMemoTagsResponse = restTemplate.postForEntity(
            uri,
            aiCreateMemoRequest,
            AICreateMemoResponse.class
        );
        return aiCreateMemoTagsResponse.getBody();
    }

    public AICreateMemosResponse createMemosTags(String content, String userId) {
        final URI uri = buildUri("/kakao-parser/");
        final String type = content.substring(content.length() - 3);
        AICreateMemosRequest aiCreateMemosRequest = AICreateMemosRequest.from(content, type, userId);
        ResponseEntity<AICreateMemosResponse> aiCreateKakaoMemosResponse = restTemplate.postForEntity(
            uri,
            aiCreateMemosRequest,
            AICreateMemosResponse.class
        );
        return aiCreateKakaoMemosResponse.getBody();
    }

    public AISearchMemoResponse searchMemo(String content, String userId) {
        final URI uri = buildUri("/search/");
        AISearchMemoRequest aiSearchMemoRequest = AISearchMemoRequest.from(content, userId);
        ResponseEntity<AISearchMemoResponse> aiSearchMemoResponse = restTemplate.postForEntity(
            uri,
            aiSearchMemoRequest,
            AISearchMemoResponse.class
        );
        return aiSearchMemoResponse.getBody();
    }

    public AICreateEmbeddingResponse createEmbedding(String name) {
        final URI uri = buildUri("/get_embedding/");
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
