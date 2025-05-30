package com.example.oatnote.memotag.service.relation.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
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

    @Indexed
    @Field("uId")
    private String userId;

    @Field("cTime")
    private LocalDateTime createdAt;

    private MemoTagRelation(
        String memoId,
        String tagId,
        boolean isLinked,
        String userId
    ) {
        this.id = UUID.randomUUID().toString();
        this.memoId = memoId;
        this.tagId = tagId;
        this.isLinked = isLinked;
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
    }

    public static MemoTagRelation of(
        String memoId,
        String tagId,
        boolean isLinked,
        String userId
    ) {
        return new MemoTagRelation(
            memoId,
            tagId,
            isLinked,
            userId
        );
    }
}
