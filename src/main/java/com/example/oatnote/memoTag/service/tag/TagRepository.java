package com.example.oatnote.memoTag.service.tag;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.oatnote.memoTag.service.tag.model.Tag;

public interface TagRepository extends MongoRepository<Tag, UUID> {

}
