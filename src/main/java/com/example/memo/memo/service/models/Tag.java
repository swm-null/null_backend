package com.example.memo.memo.service.models;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private List<String> memos;

    private String parent;

    @NotNull
    private List<String> child;

    @NotNull
    private List<Double> embedding;

    public void addMemoId(String memoId) {
        memos.add(memoId);
    }

    public void deleteMemoId(String memoId) {
        memos.remove(memoId);
    }
}
