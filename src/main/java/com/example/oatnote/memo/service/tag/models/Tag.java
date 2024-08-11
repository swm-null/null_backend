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
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Document(collection = "tags")
public class Tag {

    @Id
    private String id;

    @NotBlank(message = "태그 이름은 비워둘 수 없습니다.")
    private String name;

    @Field("parent")
    private String parentTagId;

    @Field("child")
    @NotNull(message = "자식 태그 아이디는 null일 수 없습니다.")
    private List<String> childTagIds = new LinkedList<>();

    @Field("uid")
    @Indexed
    private String userId;

    @NotEmpty(message = "임베딩값은 비워둘 수 없습니다.")
    private List<Double> embedding = new ArrayList<>();

    @Field("ca")
    @CreatedDate
    private LocalDateTime createdAt;

    @Field("ua")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public void addChildTagId(String childTagId) {
        childTagIds.add(childTagId);
    }

    public void update(String name, List<Double> embedding) {
        this.name = name;
        this.embedding = embedding;
    }
}
