package com.example.oatnote.domain.memotag.service.searchhistory.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.example.oatnote.domain.memotag.dto.SearchMemosResponse;

import lombok.Getter;

@Getter
@Document(collection = "search_histories")
public class SearchHistory {

    @Id
    private String id;

    @Field("query")
    private String query;

    @Field("searchMRes")
    private SearchMemosResponse searchMemosResponse;

    @Field("uId")
    private String userId;

    @Field("cTime")
    private LocalDateTime createdAt;

    public SearchHistory(String query, SearchMemosResponse searchMemosResponse, String userId) {
        this.id = UUID.randomUUID().toString();
        this.query = query;
        this.searchMemosResponse = searchMemosResponse;
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
    }
}
