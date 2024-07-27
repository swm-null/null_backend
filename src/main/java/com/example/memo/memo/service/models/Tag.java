package com.example.memo.memo.service.models;

import java.util.ArrayList;
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
    @Builder.Default
    private List<String> memoIds = new LinkedList<>();

    @NotNull
    @Builder.Default
    private int depth = 1;

    @Field("parent")
    private String parentTagId;

    @NotNull
    @Field("child")
    @Builder.Default
    private List<String> childTagIds = new LinkedList<>();

    @NotNull
    @Builder.Default
    private List<Double> embedding = new ArrayList<>();

    public void addMemoId(String memoId) {
        memoIds.add(memoId);
    }

    public void deleteMemoId(String memoId) {
        memoIds.remove(memoId);
    }

    public void addChildTagId(String childTagId) {
        childTagIds.add(childTagId);
    }

    public void deleteChildTagId(String childTagId) {
        childTagIds.remove(childTagId);
    }
}
