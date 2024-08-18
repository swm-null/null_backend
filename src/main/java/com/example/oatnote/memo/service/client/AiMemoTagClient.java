package com.example.oatnote.memo.service.client;

import java.net.URI;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.oatnote.memo.service.client.exception.InvalidFileException;
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

    public AiCreateKakaoMemosResponse createKakaoMemos(MultipartFile file) {
        final URI uri = buildUri("/kakao-parser/");
        String fileContent = getFileContent(file);
        String fileType = getFileType(file.getOriginalFilename());
        AiCreateKakaoMemosRequest aiCreateKakaoMemosRequest = AiCreateKakaoMemosRequest.from(fileType, fileContent);
        ResponseEntity<AiCreateKakaoMemosResponse> aiCreateKakaoMemosResponse = restTemplate.postForEntity(
            uri,
            aiCreateKakaoMemosRequest,
            AiCreateKakaoMemosResponse.class
        );
        return aiCreateKakaoMemosResponse.getBody();
    }

    private String getFileContent(MultipartFile file) {
        try {
            return new String(file.getBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new InvalidFileException("파일을 읽는 중 오류가 발생했습니다.");
        }
    }

    private String getFileType(String originalFilename) {
        if (originalFilename == null) {
            throw new InvalidFileException("파일 이름이 없습니다.");
        } else if (originalFilename.endsWith(".csv")) {
            return "csv";
        } else if (originalFilename.endsWith(".txt")) {
            return "txt";
        } else {
            throw new InvalidFileException("지원하지 않는 파일 형식입니다. csv 또는 txt 파일만 허용됩니다.");
        }
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
