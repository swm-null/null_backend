package com.example.oatnote.memo.service.tag.edge;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.oatnote.memo.service.tag.edge.model.TagEdge;

public interface TagEdgeRepository extends MongoRepository<TagEdge, String> {

    Optional<TagEdge> findByUserId(String userId);

    void deleteByUserId(String userId);
}
