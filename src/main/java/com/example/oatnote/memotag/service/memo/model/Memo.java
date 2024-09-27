package com.example.oatnote.memotag.service.memo.model;

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

    private String metadata;

    private List<Double> embedding;

    @Field("cTime")
    private LocalDateTime createdAt;

    @Field("uTime")
    private LocalDateTime updatedAt;

    public Memo(String content, List<String> imageUrls, String userId, LocalDateTime now) {
        this.id = UUID.randomUUID().toString();
        this.content = content;
        this.imageUrls = imageUrls;
        this.userId = userId;
        this.createdAt = now;
        this.updatedAt = now;
    }

    public Memo(String content, List<String> imageUrls, String userId, String metadata, List<Double> embedding) {

    }

    public void update(String content, List<String> imageUrls, String metadata, List<Double> embedding) {
        this.content = content;
        this.imageUrls = imageUrls;
        this.metadata = metadata;
        this.embedding = embedding;
        this.updatedAt = LocalDateTime.now();
    }

    public void process(String content, String metadata, List<Double> embedding) {
    }
}
