package com.example.memo.memo.service.models;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.persistence.Id;
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

    @Id
    @Field("_id")
    private String id;

    @Field("content")
    private String content;

    @Field("tags")
    private List<String> tags;

    public void updateTags(List<String> tags) {
        this.tags = tags;
    }

    public void update(String memoId, List<String> tags) {
        this.id = memoId;
        this.tags = tags;
    }
}
