package com.example.oatnote.memoTag.service.memo.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @CreatedDate
    private LocalDateTime createdAt;

    @Field("uTime")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public void update(String content, List<String> imageUrls, List<Double> embedding) {
        this.content = content;
        this.imageUrls = imageUrls;
        this.embedding = embedding;
    }
}
