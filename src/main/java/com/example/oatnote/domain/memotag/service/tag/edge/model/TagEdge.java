package com.example.oatnote.domain.memotag.service.tag.edge.model;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;

@Getter
@Document(collection = "tag_edges")
public class TagEdge {

    @Id
    private String id;

    @Indexed(unique = true)
    @Field("uId")
    private String userId;

    @Field("edges")
    private Map<String, List<String>> tagEdges;

    public TagEdge(String userId, Map<String, List<String>> tagEdges) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.tagEdges = tagEdges;
    }
}
