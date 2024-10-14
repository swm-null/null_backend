package com.example.oatnote.domain.memotag.service.tag.relation.model;

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

    @Indexed
    @Field("uId")
    private String userId;

    @Field("cTime")
    private LocalDateTime createdAt;

    public TagsRelation(String parentTagId, String childTagId, String userId) {
        this.id = UUID.randomUUID().toString();
        this.parentTagId = parentTagId;
        this.childTagId = childTagId;
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
    }
}
