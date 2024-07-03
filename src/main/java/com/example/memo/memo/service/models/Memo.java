package com.example.memo.memo.service.models;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Document(collection = "memos")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Memo {

    @MongoId
    @Field("_id")
    private String id;

    @NotBlank
    @Field("content")
    private String content;

    @Field("tags")
    private List<String> tags;

    public void update(String content, List<String> tags) {
        this.content = content;
        this.tags = tags;
    }
}
