package com.example.oatnote.domain.memotag.service.memo.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Document(collection = "memos")
public class Memo {

    @Id
    private String id;

    private String content;

    @Field("imgs")
    private List<String> imageUrls;

    @Field("voices")
    private List<String> voiceUrls;

    @Indexed
    @Field("uId")
    private String userId;

    private String metadata;

    private List<Double> embedding;

    private List<Double> embeddingMetadata;

    @Field("cTime")
    private LocalDateTime createdAt;

    @Field("uTime")
    private LocalDateTime updatedAt;

    public Memo(
        String id,
        String content,
        List<String> imageUrls,
        List<String> voiceUrls,
        String userId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        this.id = id;
        this.content = content;
        this.imageUrls = imageUrls;
        this.voiceUrls = voiceUrls;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Memo(
        String content,
        List<String> imageUrls,
        List<String> voiceUrls,
        String userId,
        LocalDateTime time
    ) {
        this.id = UUID.randomUUID().toString();
        this.content = content;
        this.imageUrls = imageUrls;
        this.voiceUrls = voiceUrls;
        this.userId = userId;
        this.createdAt = time;
        this.updatedAt = time;
    }

    public Memo(
        String id,
        String content,
        List<String> imageUrls,
        List<String> voiceUrls,
        String userId,
        String metadata,
        List<Double> embedding,
        List<Double> embeddingMetadata,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {
        this.id = id != null ? id : UUID.randomUUID().toString();
        this.content = content;
        this.imageUrls = imageUrls;
        this.voiceUrls = voiceUrls;
        this.userId = userId;
        this.metadata = metadata;
        this.embedding = embedding;
        this.embeddingMetadata = embeddingMetadata;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void update(
        String content,
        List<String> imageUrls,
        List<String> voiceUrls
    ) {
        this.content = content;
        this.imageUrls = imageUrls;
        this.voiceUrls = voiceUrls;
        this.updatedAt = LocalDateTime.now();
    }

    public void update(
        String content,
        List<String> imageUrls,
        List<String> voiceUrls,
        String metadata,
        List<Double> embedding,
        List<Double> embeddingMetadata
    ) {
        this.content = content;
        this.imageUrls = imageUrls;
        this.voiceUrls = voiceUrls;
        this.metadata = metadata;
        this.embedding = embedding;
        this.embeddingMetadata = embeddingMetadata;
        this.updatedAt = LocalDateTime.now();
    }

    public Memo process(
        String metadata,
        List<Double> embedding,
        List<Double> embeddingMetadata
    ) {
        this.metadata = metadata;
        this.embedding = embedding;
        this.embeddingMetadata = embeddingMetadata;
        return this;
    }
}
