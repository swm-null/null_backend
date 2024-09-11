package com.example.oatnote.memoTag.service.memo.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;

@Getter
@Document(collection = "memos")
public class Memo {

    @Id
    private String id;

    private String content;

    @Field("imgs")
    private List<String> imageUrls;

    @Indexed
    @Field("uId")
    private String userId;

    private List<Double> embedding;

    @Field("cTime")
    private LocalDateTime createdAt;

    @Field("uTime")
    private LocalDateTime updatedAt;

    public Memo(String content, List<String> imageUrls, String userId, List<Double> embedding) {
        this.id = UUID.randomUUID().toString();
        this.content = content;
        this.imageUrls = imageUrls;
        this.userId = userId;
        this.embedding = embedding;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void update(String content, List<String> imageUrls, List<Double> embedding) {
        this.content = content;
        this.imageUrls = imageUrls;
        this.embedding = embedding;
        this.updatedAt = LocalDateTime.now();
    }
}
