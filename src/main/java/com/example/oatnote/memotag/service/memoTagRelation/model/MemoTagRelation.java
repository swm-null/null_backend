package com.example.oatnote.memotag.service.memoTagRelation.model;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;

@Getter
@Document(collection = "memo_tag_relations")
public class MemoTagRelation {

    @Id
    private String id;

    @Indexed
    @Field("mId")
    private String memoId;

    @Indexed
    @Field("tId")
    private String tagId;

    @Indexed
    @Field("isL")
    private boolean isLinked;

    public MemoTagRelation(String memoId, String tagId, boolean isLinked) {
        this.id = UUID.randomUUID().toString();
        this.memoId = memoId;
        this.tagId = tagId;
        this.isLinked = isLinked;
    }
}
