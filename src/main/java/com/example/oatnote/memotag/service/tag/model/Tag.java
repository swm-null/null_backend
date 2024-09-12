package com.example.oatnote.memotag.service.tag.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;

@Getter
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

    public Tag(String id, String name, String userId, List<Double> embedding) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.embedding = embedding;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void update(String name, List<Double> embedding) {
        this.name = name;
        this.embedding = embedding;
        this.updatedAt = LocalDateTime.now();
    }
}
