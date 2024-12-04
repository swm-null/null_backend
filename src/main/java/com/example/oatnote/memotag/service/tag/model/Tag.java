package com.example.oatnote.memotag.service.tag.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Document(collection = "tags")
public class Tag {

    @Id
    private String id;

    @Indexed
    private String name;

    @Field("uId")
    @Indexed
    private String userId;

    private List<Double> embedding;

    @Field("cTime")
    private LocalDateTime createdAt;

    @Field("uTime")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Tag(String id, String name, String userId) {
        this.id = Objects.requireNonNullElse(id, UUID.randomUUID().toString());
        this.name = name;
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Tag(String id, String name, String userId, List<Double> embedding) {
        this.id = Objects.requireNonNullElse(id, UUID.randomUUID().toString());
        this.name = name;
        this.userId = userId;
        this.embedding = embedding;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public static Tag of(String name, String userId) {
        return new Tag(userId, name, userId);
    }

    public void update(String name, List<Double> embedding) {
        this.name = name;
        this.embedding = embedding;
        this.updatedAt = LocalDateTime.now();
    }
}
