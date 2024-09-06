package com.example.oatnote.memoTag.service.tagsRelation.model;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;

@Getter
@Document(collection = "tag_relations")
public class TagsRelation {

    @Id
    private UUID id;

    @Indexed
    @Field("pTId")
    private String parentTagId;

    @Indexed
    @Field("cTId")
    private String childTagId;

    public TagsRelation(String parentTagId, String childTagId) {
        this.id = UUID.randomUUID();
        this.parentTagId = parentTagId;
        this.childTagId = childTagId;
    }
}
