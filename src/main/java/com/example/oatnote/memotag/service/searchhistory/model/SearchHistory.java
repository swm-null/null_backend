package com.example.oatnote.memotag.service.searchhistory.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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

    @Field("aiDesc")
    private String aiDescription;

    @Field("aiMIds")
    private List<String> aiMemoIds;

    @Field("dbMIds")
    private List<String> dbMemoIds;

    @Field("uId")
    private String userId;

    @Field("cTime")
    private LocalDateTime createdAt;

    public SearchHistory(
        String query,
        String userId
    ) {
        this.id = UUID.randomUUID().toString();
        this.query = query;
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
    }
}
