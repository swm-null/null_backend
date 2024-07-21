package com.example.memo.memo.service.models;

import java.util.LinkedList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Document(collection = "memos")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Memo {

    @Id
    private String id;

    @NotBlank
    private String content;

    @NotNull
    @Field("tags")
    private List<String> tagIds = new LinkedList<>();

    @NotNull
    private List<Double> embedding;

    public void updateTags(List<String> tagIds) {
        this.tagIds = tagIds;
    }

    public void update(String content, List<Double> embedding) {
        this.content = content;
        this.embedding = embedding;
    }
}
