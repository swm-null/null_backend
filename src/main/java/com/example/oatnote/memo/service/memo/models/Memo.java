package com.example.oatnote.memo.service.memo.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Document(collection = "memos")
public class Memo {

    @Id
    private String id;

    @NotBlank
    private String content;

    private List<String> imageUrls = new ArrayList<>();

    @Indexed
    private String userId;

    @NotNull
    private List<Double> embedding = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public void update(String content, List<Double> embedding) {
        this.content = content;
        this.embedding = embedding;
    }
}
