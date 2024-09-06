package com.example.oatnote.memoTag.service.tagEdge.model;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Document(collection = "tag_edges")
public class TagEdge {

    @Id
    private UUID id;

    @Indexed(unique = true)
    @Field("uId")
    private String userId;

    @Field("edges")
    private Map<String, List<String>> tagEdges;
}
