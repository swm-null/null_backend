package com.example.oatnote.memotag.service.tagsRelation.model;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;

@Getter
@Document(collection = "tags_relations")
public class TagsRelation {

    @Id
    private String id;

    @Indexed
    @Field("pTId")
    private String parentTagId;

    @Indexed
    @Field("cTId")
    private String childTagId;

    public TagsRelation(String parentTagId, String childTagId) {
        this.id = UUID.randomUUID().toString();
        this.parentTagId = parentTagId;
        this.childTagId = childTagId;
    }
}
