package com.example.oatnote.domain.memotag.service.tag.edge.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Document(collection = "tag_edges")
public class TagEdge {

    @Id
    private String id;

    private Map<String, List<String>> edges;

    private Map<String, List<String>> reversedEdges;

    @Indexed(unique = true)
    @Field("uId")
    private String userId;

    @Field("cTime")
    private LocalDateTime createdAt;

    private TagEdge(
        Map<String, List<String>> edges,
        Map<String, List<String>> reversedEdges,
        String userId
    ) {
        this.id = UUID.randomUUID().toString();
        this.edges = edges;
        this.reversedEdges = reversedEdges;
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
    }

    public static TagEdge of(
        String userId,
        Map<String, List<String>> edges,
        Map<String, List<String>> reversedEdges
    ) {
        return new TagEdge(edges, reversedEdges, userId);
    }

    public void updateEdges(Map<String, List<String>> tagEdges) {
        this.edges = tagEdges;
    }
}
