package com.example.oatnote.domain.memotag.service.aiClient;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.oatnote.domain.memotag.service.aiClient.dto.AiCreateEmbeddingRequest;
import com.example.oatnote.domain.memotag.service.aiClient.dto.AiCreateEmbeddingResponse;
import com.example.oatnote.domain.memotag.service.aiClient.dto.AiCreateMetadataRequest;
import com.example.oatnote.domain.memotag.service.aiClient.dto.AiCreateMetadataResponse;
import com.example.oatnote.domain.memotag.service.aiClient.dto.AiCreateStructureRequest;
import com.example.oatnote.domain.memotag.service.aiClient.dto.AiCreateStructureResponse;
import com.example.oatnote.domain.memotag.service.aiClient.dto.AiCreateTagsRequest;
import com.example.oatnote.domain.memotag.service.aiClient.dto.AiCreateTagsResponse;
import com.example.oatnote.domain.memotag.service.aiClient.dto.AiSearchMemosUsingAiRequest;
import com.example.oatnote.domain.memotag.service.aiClient.dto.AiSearchMemosUsingAiResponse;
import com.example.oatnote.domain.memotag.service.aiClient.dto.AiSearchMemosUsingDbRequest;
import com.example.oatnote.domain.memotag.service.aiClient.dto.AiSearchMemosUsingDbResponse;

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

    public AiCreateTagsResponse createTags(AiCreateTagsRequest aiCreateTagsRequest) {
        final URI uri = buildUri("/memo/tag");
        ResponseEntity<AiCreateTagsResponse> aiCreateTagsResponse = restTemplate.postForEntity(
            uri,
            aiCreateTagsRequest,
            AiCreateTagsResponse.class
        );
        return aiCreateTagsResponse.getBody();
    }

    public AiCreateStructureResponse createStructure(AiCreateStructureRequest aiCreateStructureRequest) {
        final URI uri = buildUri("/memo/structures");
        ResponseEntity<AiCreateStructureResponse> aiCreateStructureResponse = restTemplate.postForEntity(
            uri,
            aiCreateStructureRequest,
            AiCreateStructureResponse.class
        );
        return aiCreateStructureResponse.getBody();
    }

    public AiCreateStructureResponse createStructure(String fileUrl, String userId) {
        final URI uri = buildUri("/kakao-parser");
        final String type = fileUrl.substring(fileUrl.lastIndexOf('.') + 1).toLowerCase();
        AiCreateStructureRequest aiCreateStructureRequest = AiCreateStructureRequest.from(fileUrl, type, userId);
        ResponseEntity<AiCreateStructureResponse> aiCreateStructureResponse = restTemplate.postForEntity(
            uri,
            aiCreateStructureRequest,
            AiCreateStructureResponse.class
        );
        return aiCreateStructureResponse.getBody();
    }

    public AiSearchMemosUsingAiResponse searchMemoUsingAi(String query, String userId) {
        final URI uri = buildUri("/search/ai");
        AiSearchMemosUsingAiRequest aiSearchMemosUsingAiRequest = AiSearchMemosUsingAiRequest.of(query, userId);
        ResponseEntity<AiSearchMemosUsingAiResponse> aiSearchMemosUsingAiResponse = restTemplate.postForEntity(
            uri,
            aiSearchMemosUsingAiRequest,
            AiSearchMemosUsingAiResponse.class
        );
        return aiSearchMemosUsingAiResponse.getBody();
    }

    public AiSearchMemosUsingDbResponse searchMemoUsingDb(String query, String userId) {
        final URI uri = buildUri("/search/db");
        AiSearchMemosUsingDbRequest aiSearchMemosUsingDbRequest = AiSearchMemosUsingDbRequest.of(query, userId);
        ResponseEntity<AiSearchMemosUsingDbResponse> aiSearchMemosUsingDbResponse = restTemplate.postForEntity(
            uri,
            aiSearchMemosUsingDbRequest,
            AiSearchMemosUsingDbResponse.class
        );
        return aiSearchMemosUsingDbResponse.getBody();
    }

    public AiCreateEmbeddingResponse createEmbedding(String content) {
        final URI uri = buildUri("/get-embedding");
        AiCreateEmbeddingRequest aiCreateEmbeddingRequest = AiCreateEmbeddingRequest.from(content);
        ResponseEntity<AiCreateEmbeddingResponse> aiCreateEmbeddingResponse = restTemplate.postForEntity(
            uri,
            aiCreateEmbeddingRequest,
            AiCreateEmbeddingResponse.class
        );
        return aiCreateEmbeddingResponse.getBody();
    }

    public AiCreateMetadataResponse createMetadata(String content, List<String> imageUrls, List<String> voiceUrls) {
        final URI uri = buildUri("/get-metadata-with-embedding");
        AiCreateMetadataRequest aiCreateMetadataRequest = AiCreateMetadataRequest.from(content, imageUrls, voiceUrls);
        ResponseEntity<AiCreateMetadataResponse> aiCreateMetadataResponse = restTemplate.postForEntity(
            uri,
            aiCreateMetadataRequest,
            AiCreateMetadataResponse.class
        );
        return aiCreateMetadataResponse.getBody();
    }

    URI buildUri(String path) {
        return UriComponentsBuilder
            .fromUriString(aiUrl)
            .path(path)
            .encode()
            .build()
            .toUri();
    }
}
