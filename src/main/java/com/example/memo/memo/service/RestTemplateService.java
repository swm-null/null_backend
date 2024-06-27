package com.example.memo.memo.service;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.memo.memo.service.models.AiResponse;
import com.example.memo.memo.service.models.MemoRequestBridge;
import com.example.memo.memo.service.models.SaveResponse;

@Service
public class RestTemplateService {
    RestTemplate restTemplate = new RestTemplate();

    @Value("${AI_URL}")
    private String aiUrl;

    public ResponseEntity<SaveResponse> getTags(MemoRequestBridge memoRequestBridge) {
        String uri = aiUrl + "/add_memo/";
        ResponseEntity<SaveResponse> aiResponse = restTemplate.postForEntity(uri, memoRequestBridge, SaveResponse.class);
        return aiResponse;
    }

    public AiResponse searchMemo(String content) {
        System.out.println(content);

        String uri = aiUrl + "/user_query?query=" + content;

        System.out.println(uri);
        ResponseEntity<AiResponse> aiResponse = restTemplate.getForEntity(uri, AiResponse.class);
        if (aiResponse.getStatusCode().is5xxServerError()) {
            throw new RuntimeException("AI 서비스 서버 에러");
        } else if (aiResponse.getStatusCode().is4xxClientError() || aiResponse.getBody() == null) {
            throw new RuntimeException("AI 서비스 클라이언트 에러");
        }
        return aiResponse.getBody();
    }
}
