package com.example.oatnote.memoTag.service.tagEdge;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.oatnote.memoTag.service.tagEdge.model.TagEdge;

public interface TagEdgeRepository extends MongoRepository<TagEdge, String> {

}
