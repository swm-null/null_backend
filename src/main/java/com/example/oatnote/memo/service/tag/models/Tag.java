package com.example.oatnote.memo.service.tag.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
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
@Document(collection = "tags")
public class Tag {

    @Id
    private String id;

    @NotBlank
    private String name;

    @Field("parent")
    private String parentTagId;

    @Field("child")
    private List<String> childTagIds = new LinkedList<>();

    @Indexed
    private String userId;

    @NotNull
    private List<Double> embedding = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public void addChildTagId(String childTagId) {
        childTagIds.add(childTagId);
    }

    public void deleteChildTagId(String childTagId) {
        childTagIds.remove(childTagId);
    }

    public void update(String name, List<Double> embedding) {
        this.name = name;
        this.embedding = embedding;
    }
}
