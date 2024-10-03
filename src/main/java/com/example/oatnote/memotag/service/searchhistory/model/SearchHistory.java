package com.example.oatnote.memotag.service.searchhistory.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.example.oatnote.memotag.dto.SearchMemosResponse;
import com.example.oatnote.memotag.dto.innerDto.MemoResponse;

import lombok.Getter;

@Getter
@Document(collection = "search_histories")
public class SearchHistory {

    @Id
    private String id;

    @Field("searchTerm")
    private String searchTerm;

    @Field("searchMRes")
    private SearchMemosResponse searchMemosResponse;

    @Field("uId")
    private String userId;

    @Field("cTime")
    private LocalDateTime createdAt;

    public SearchHistory(String searchTerm, SearchMemosResponse searchMemosResponse, String userId) {
        this.id = UUID.randomUUID().toString();
        this.searchTerm = searchTerm;
        this.searchMemosResponse = searchMemosResponse;
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
    }
}
