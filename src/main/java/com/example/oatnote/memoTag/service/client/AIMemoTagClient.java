package com.example.oatnote.memoTag.service.client;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.oatnote.memoTag.service.client.models.AICreateMemosTagsRequest;
import com.example.oatnote.memoTag.service.client.models.AICreateMemosTagsResponse;
import com.example.oatnote.memoTag.service.client.models.AICreateMemoTagsRequest;
import com.example.oatnote.memoTag.service.client.models.AICreateMemoTagsResponse;
import com.example.oatnote.memoTag.service.client.models.AICreateTagRequest;
import com.example.oatnote.memoTag.service.client.models.AICreateTagResponse;
import com.example.oatnote.memoTag.service.client.models.AISearchMemoRequest;
import com.example.oatnote.memoTag.service.client.models.AISearchMemoResponse;

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

    public AICreateMemoTagsResponse createMemo(String content) {
        final URI uri = buildUri("/memos");
        AICreateMemoTagsRequest aiCreateMemoTagsRequest = AICreateMemoTagsRequest.from(content, null);
        ResponseEntity<AICreateMemoTagsResponse> aiCreateMemoResponse = restTemplate.postForEntity(
            uri,
            aiCreateMemoTagsRequest,
            AICreateMemoTagsResponse.class
        );
        return aiCreateMemoResponse.getBody();
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

    public AICreateTagResponse createTag(String name) {
        final URI uri = buildUri("/get_embedding");
        AICreateTagRequest aiCreateTagRequest = AICreateTagRequest.from(name);
        ResponseEntity<AICreateTagResponse> aiCreateTagResponse = restTemplate.postForEntity(
            uri,
            aiCreateTagRequest,
            AICreateTagResponse.class
        );
        return aiCreateTagResponse.getBody();
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
