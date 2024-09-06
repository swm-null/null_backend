package com.example.oatnote.memoTag.service.memoTagRelation.model;

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
    private UUID id;

    @Indexed
    @Field("mId")
    private UUID memoId;

    @Indexed
    @Field("tId")
    private UUID tagId;

    @Indexed
    @Field("isL")
    private boolean isLinked;

    public MemoTagRelation(UUID memoId, UUID tagId, boolean isLinked) {
        this.id = UUID.randomUUID();
        this.memoId = memoId;
        this.tagId = tagId;
        this.isLinked = isLinked;
    }
}
