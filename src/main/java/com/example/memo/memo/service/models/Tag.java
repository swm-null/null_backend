package com.example.memo.memo.service.models;

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
@Document(collection = "tags")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {

    @Id
    private String id;

    @NotBlank
    private String name;

    @NotNull
    @Field("memos")
    private List<String> memoIds;

    @Field("parent")
    private String parentId;

    @NotNull
    @Field("child")
    private List<String> childIds;

    @NotNull
    private List<Double> embedding;

    public void addMemoId(String memoId) {
        memoIds.add(memoId);
    }

    public void deleteMemoId(String memoId) {
        memoIds.remove(memoId);
    }
}
