package com.example.oatnote.domain.memotag.service.client;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.oatnote.domain.memotag.service.client.dto.AICreateEmbeddingRequest;
import com.example.oatnote.domain.memotag.service.client.dto.AICreateEmbeddingResponse;
import com.example.oatnote.domain.memotag.service.client.dto.AICreateMetadataRequest;
import com.example.oatnote.domain.memotag.service.client.dto.AICreateMetadataResponse;
import com.example.oatnote.domain.memotag.service.client.dto.AICreateStructureRequest;
import com.example.oatnote.domain.memotag.service.client.dto.AICreateStructureResponse;
import com.example.oatnote.domain.memotag.service.client.dto.AICreateTagsRequest;
import com.example.oatnote.domain.memotag.service.client.dto.AICreateTagsResponse;
import com.example.oatnote.domain.memotag.service.client.dto.AICreateMemosRequest;
import com.example.oatnote.domain.memotag.service.client.dto.AICreateMemosResponse;
import com.example.oatnote.domain.memotag.service.client.dto.AISearchMemosUsingAiRequest;
import com.example.oatnote.domain.memotag.service.client.dto.AISearchMemosUsingAiResponse;

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

    public AICreateStructureResponse createStructure(AICreateStructureRequest aiCreateStructureRequest) {
        final URI uri = buildUri("/memo/structures");
        ResponseEntity<AICreateStructureResponse> aiCreateStructureResponse = restTemplate.postForEntity(
            uri,
            aiCreateStructureRequest,
            AICreateStructureResponse.class
        );
        return aiCreateStructureResponse.getBody();
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

    public AISearchMemosUsingAiResponse searchMemoUsingAi(String query, String userId) {
        final URI uri = buildUri("/search/ai");
        AISearchMemosUsingAiRequest aiSearchMemosUsingAiRequest = AISearchMemosUsingAiRequest.of(query, userId);
        ResponseEntity<AISearchMemosUsingAiResponse> aiSearchMemosUsingAiResponse = restTemplate.postForEntity(
            uri,
            aiSearchMemosUsingAiRequest,
            AISearchMemosUsingAiResponse.class
        );
        return aiSearchMemosUsingAiResponse.getBody();
    }

    public AICreateEmbeddingResponse createEmbedding(String content) {
        final URI uri = buildUri("/get-embedding");
        AICreateEmbeddingRequest aiCreateEmbeddingRequest = AICreateEmbeddingRequest.from(content);
        ResponseEntity<AICreateEmbeddingResponse> aiCreateEmbeddingResponse = restTemplate.postForEntity(
            uri,
            aiCreateEmbeddingRequest,
            AICreateEmbeddingResponse.class
        );
        return aiCreateEmbeddingResponse.getBody();
    }

    public AICreateMetadataResponse createMetadata(String content, List<String> imageUrls) {
        final URI uri = buildUri("/get-metadata-with-embedding");
        AICreateMetadataRequest aiCreateMetadataRequest = AICreateMetadataRequest.from(content, imageUrls);
        ResponseEntity<AICreateMetadataResponse> aiCreateMetadataResponse = restTemplate.postForEntity(
            uri,
            aiCreateMetadataRequest,
            AICreateMetadataResponse.class
        );
        return aiCreateMetadataResponse.getBody();
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
