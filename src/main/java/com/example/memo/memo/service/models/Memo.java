package com.example.memo.memo.service.models;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

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

    @MongoId
    private ObjectId id;

    @NotBlank
    private String content;

    @NotNull
    private List<ObjectId> tags;

    @NotNull
    private List<Double> embedding;

    public void updateTags(List<ObjectId> tagIds) {
        this.tags = tagIds;
    }
}
