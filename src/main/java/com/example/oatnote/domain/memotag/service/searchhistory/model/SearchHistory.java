package com.example.oatnote.domain.memotag.service.searchhistory.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.example.oatnote.domain.memotag.dto.SearchMemosUsingAiResponse;
import com.example.oatnote.domain.memotag.dto.innerDto.MemoResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Document(collection = "search_histories")
public class SearchHistory {

    @Id
    private String id;

    @Field("query")
    private String query;

    @Field("pMsg")
    private String processedMessage;

    private List<MemoResponse> memos;

    @Field("uId")
    private String userId;

    @Field("cTime")
    private LocalDateTime createdAt;

    public SearchHistory(
        String query,
        String processedMessage,
        List<MemoResponse> memos,
        String userId
    ) {
        this.id = UUID.randomUUID().toString();
        this.query = query;
        this.processedMessage = processedMessage;
        this.memos = memos;
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
    }
}
