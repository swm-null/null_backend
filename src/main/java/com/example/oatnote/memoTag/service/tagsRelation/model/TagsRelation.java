package com.example.oatnote.memoTag.service.tagsRelation.model;

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
    private UUID id;

    @Indexed
    @Field("pTId")
    private UUID parentTagId;

    @Indexed
    @Field("cTId")
    private UUID childTagId;

    public TagsRelation(UUID parentTagId, UUID childTagId) {
        this.id = UUID.randomUUID();
        this.parentTagId = parentTagId;
        this.childTagId = childTagId;
    }
}
