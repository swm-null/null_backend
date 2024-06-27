package com.example.memo.memo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.memo.memo.service.models.AiSearchResponse;
import com.example.memo.memo.service.models.MemoRequestBridge;
import com.example.memo.memo.service.models.AiSaveResponse;

@Service
public class RestTemplateService {
    RestTemplate restTemplate = new RestTemplate();

    @Value("${AI_URL}")
    private String aiUrl;

    public AiSaveResponse getTags(MemoRequestBridge memoRequestBridge) {
        String uri = aiUrl + "/add_memo/";
        ResponseEntity<AiSaveResponse> aiResponse = restTemplate.postForEntity(uri, memoRequestBridge,
            AiSaveResponse.class);
        return aiResponse.getBody();
    }

    public AiSearchResponse searchMemo(String content) {
        System.out.println(content);

        String uri = aiUrl + "/user_query?query=" + content;

        System.out.println(uri);
        ResponseEntity<AiSearchResponse> aiResponse = restTemplate.getForEntity(uri, AiSearchResponse.class);
        if (aiResponse.getStatusCode().is5xxServerError()) {
            throw new RuntimeException("AI 서비스 서버 에러");
        } else if (aiResponse.getStatusCode().is4xxClientError() || aiResponse.getBody() == null) {
            throw new RuntimeException("AI 서비스 클라이언트 에러");
        }
        return aiResponse.getBody();
    }
}
