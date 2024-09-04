package com.example.oatnote.memoTag.service.client;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.oatnote.memoTag.service.client.models.AiCreateMemosTagsRequest;
import com.example.oatnote.memoTag.service.client.models.AiCreateMemosTagsResponse;
import com.example.oatnote.memoTag.service.client.models.AiCreateMemoTagsRequest;
import com.example.oatnote.memoTag.service.client.models.AiCreateMemoTagsResponse;
import com.example.oatnote.memoTag.service.client.models.AiCreateTagRequest;
import com.example.oatnote.memoTag.service.client.models.AiCreateTagResponse;
import com.example.oatnote.memoTag.service.client.models.AiSearchMemoRequest;
import com.example.oatnote.memoTag.service.client.models.AiSearchMemoResponse;

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

    public AiCreateMemoTagsResponse createMemo(String content) {
        final URI uri = buildUri("/memos");
        AiCreateMemoTagsRequest aiCreateMemoTagsRequest = AiCreateMemoTagsRequest.from(content, null);
        ResponseEntity<AiCreateMemoTagsResponse> aiCreateMemoResponse = restTemplate.postForEntity(
            uri,
            aiCreateMemoTagsRequest,
            AiCreateMemoTagsResponse.class
        );
        return aiCreateMemoResponse.getBody();
    }

    public AiCreateMemosTagsResponse createMemosTags(String content) {
        final URI uri = buildUri("/kakao-parser");
        final String type = content.substring(content.length() - 3);
        AiCreateMemosTagsRequest aiCreateMemosTagsRequest = AiCreateMemosTagsRequest.from(content, type);
        ResponseEntity<AiCreateMemosTagsResponse> aiCreateKakaoMemosResponse = restTemplate.postForEntity(
            uri,
            aiCreateMemosTagsRequest,
            AiCreateMemosTagsResponse.class
        );
        return aiCreateKakaoMemosResponse.getBody();
    }

    public AiSearchMemoResponse searchMemo(String content) {
        final URI uri = buildUri("/search");
        AiSearchMemoRequest aiSearchMemoRequest = AiSearchMemoRequest.from(content);
        ResponseEntity<AiSearchMemoResponse> aiSearchMemoResponse = restTemplate.postForEntity(
            uri,
            aiSearchMemoRequest,
            AiSearchMemoResponse.class
        );
        return aiSearchMemoResponse.getBody();
    }

    public AiCreateTagResponse createTag(String name) {
        final URI uri = buildUri("/get_embedding");
        AiCreateTagRequest aiCreateTagRequest = AiCreateTagRequest.from(name);
        ResponseEntity<AiCreateTagResponse> aiCreateTagResponse = restTemplate.postForEntity(
            uri,
            aiCreateTagRequest,
            AiCreateTagResponse.class
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
