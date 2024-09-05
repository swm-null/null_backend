package com.example.oatnote.memoTag.service.client;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.oatnote.memoTag.service.client.dto.AICreateMemoTagsResponse;
import com.example.oatnote.memoTag.service.client.dto.AICreateMemosTagsRequest;
import com.example.oatnote.memoTag.service.client.dto.AICreateMemosTagsResponse;
import com.example.oatnote.memoTag.service.client.dto.AICreateMemoTagsRequest;
import com.example.oatnote.memoTag.service.client.dto.AICreateEmbeddingRequest;
import com.example.oatnote.memoTag.service.client.dto.AICreateEmbeddingResponse;
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

    public AICreateMemoTagsResponse createMemoTags(String content) {
        final URI uri = buildUri("/memos");
        AICreateMemoTagsRequest aiCreateMemoTagsRequest = AICreateMemoTagsRequest.from(content);
        ResponseEntity<AICreateMemoTagsResponse> aiCreateMemoTagsResponse = restTemplate.postForEntity(
            uri,
            aiCreateMemoTagsRequest,
            AICreateMemoTagsResponse.class
        );
        return aiCreateMemoTagsResponse.getBody();
    }

    public AICreateMemosTagsResponse createMemosTags(String content) {
        final URI uri = buildUri("/kakao-parser");
        final String type = content.substring(content.length() - 3);
        AICreateMemosTagsRequest aiCreateMemosTagsRequest = AICreateMemosTagsRequest.from(content, type);
        ResponseEntity<AICreateMemosTagsResponse> aiCreateKakaoMemosResponse = restTemplate.postForEntity(
            uri,
            aiCreateMemosTagsRequest,
            AICreateMemosTagsResponse.class
        );
        return aiCreateKakaoMemosResponse.getBody();
    }

    public AISearchMemoResponse searchMemo(String content) {
        final URI uri = buildUri("/search");
        AISearchMemoRequest aiSearchMemoRequest = AISearchMemoRequest.from(content);
        ResponseEntity<AISearchMemoResponse> aiSearchMemoResponse = restTemplate.postForEntity(
            uri,
            aiSearchMemoRequest,
            AISearchMemoResponse.class
        );
        return aiSearchMemoResponse.getBody();
    }

    public AICreateEmbeddingResponse createEmbedding(String name) {
        final URI uri = buildUri("/get_embedding");
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
