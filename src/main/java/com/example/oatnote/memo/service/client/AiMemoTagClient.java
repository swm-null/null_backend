package com.example.oatnote.memo.service.client;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.oatnote.memo.service.client.models.AiCreateKakaoMemosRequest;
import com.example.oatnote.memo.service.client.models.AiCreateKakaoMemosResponse;
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

    private URI buildUri(String path) {
        return UriComponentsBuilder
            .fromUriString(aiUrl)
            .path(path)
            .encode()
            .build()
            .toUri();
    }

    public AiCreateMemoResponse createMemo(String content) {
        final URI uri = buildUri("/add_memo/");
        AiCreateMemoRequest aiCreateMemoRequest = AiCreateMemoRequest.from(content);
        ResponseEntity<AiCreateMemoResponse> aiCreateMemoResponse = restTemplate.postForEntity(
            uri,
            aiCreateMemoRequest,
            AiCreateMemoResponse.class
        );
        return aiCreateMemoResponse.getBody();
    }

    public AiCreateKakaoMemosResponse createKakaoMemos(String content) {
        final URI uri = buildUri("/kakao-parser/");
        final String type = content.substring(content.length() - 3);
        AiCreateKakaoMemosRequest aiCreateKakaoMemosRequest = AiCreateKakaoMemosRequest.from(content, type);
        ResponseEntity<AiCreateKakaoMemosResponse> aiCreateKakaoMemosResponse = restTemplate.postForEntity(
            uri,
            aiCreateKakaoMemosRequest,
            AiCreateKakaoMemosResponse.class
        );
        return aiCreateKakaoMemosResponse.getBody();
    }

    public AiSearchMemoResponse searchMemo(String content) {
        final URI uri = buildUri("/search/");
        AiSearchMemoRequest aiSearchMemoRequest = AiSearchMemoRequest.from(content);
        ResponseEntity<AiSearchMemoResponse> aiSearchMemoResponse = restTemplate.postForEntity(
            uri,
            aiSearchMemoRequest,
            AiSearchMemoResponse.class
        );
        return aiSearchMemoResponse.getBody();
    }

    public AiCreateTagResponse createTag(String name) {
        final URI uri = buildUri("/get_embedding/");
        AiCreateTagRequest aiCreateTagRequest = AiCreateTagRequest.from(name);
        ResponseEntity<AiCreateTagResponse> aiCreateTagResponse = restTemplate.postForEntity(
            uri,
            aiCreateTagRequest,
            AiCreateTagResponse.class
        );
        return aiCreateTagResponse.getBody();
    }
}
