package com.example.oatnote.memotag.service.memo.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
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
        String content,
        List<String> imageUrls,
        List<String> voiceUrls,
        String userId,
        String metadata
    ) {
        this.id = UUID.randomUUID().toString();
        this.content = content;
        this.imageUrls = imageUrls;
        this.voiceUrls = voiceUrls;
        this.userId = userId;
        this.metadata = metadata;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
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
        LocalDateTime time
    ) {
        this.id = Objects.requireNonNullElse(id, UUID.randomUUID().toString());
        this.content = content;
        this.imageUrls = imageUrls;
        this.voiceUrls = voiceUrls;
        this.userId = userId;
        this.metadata = metadata;
        this.embedding = embedding;
        this.embeddingMetadata = embeddingMetadata;
        this.createdAt = Objects.requireNonNullElse(createdAt, time);
        this.updatedAt = time;
    }

    public void update(
        String content,
        List<String> imageUrls,
        List<String> voiceUrls,
        String metadata
    ) {
        this.content = content;
        this.imageUrls = imageUrls;
        this.voiceUrls = voiceUrls;
        this.metadata = metadata;
        this.updatedAt = LocalDateTime.now();
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
        this.metadata = metadata;
        this.embedding = embedding;
        this.embeddingMetadata = embeddingMetadata;
    }
}
