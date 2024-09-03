package com.example.oatnote.memoTag.service.tagRelation.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Document(collection = "tag_relations")
public class TagRelation {

    @Id
    private String id;

    @Indexed
    @Field("pTId")
    private String parentTagId;

    @Indexed
    @Field("cTId")
    private String childTagId;
}
