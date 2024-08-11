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
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Document(collection = "memos")
public class Memo {

    @Id
    private String id;

    @NotBlank(message = "내용은 비워둘 수 없습니다.")
    private String content;

    private List<String> imageUrls = new ArrayList<>();

    @Indexed
    @NotBlank(message = "유저 아이디는 비워둘 수 없습니다.")
    private String userId;

    @NotEmpty(message = "임베딩값은 비워둘 수 없습니다.")
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
