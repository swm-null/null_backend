package com.example.oatnote.memoTag.service.memoTagRelation.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
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
}
