package com.example.oatnote.memoTag.service.tag.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Document(collection = "tags")
public class Tag {

    @Id
    private UUID id;

    @Indexed
    private String name;

    @Field("uId")
    @Indexed
    private String userId;

    private List<Double> embedding;

    @Field("cTime")
    @CreatedDate
    private LocalDateTime createdAt;

    @Field("uTime")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public void update(String name, List<Double> embedding) {
        this.name = name;
        this.embedding = embedding;
    }
}
