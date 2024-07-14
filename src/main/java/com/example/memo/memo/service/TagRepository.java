package com.example.memo.memo.service;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.memo.tag.service.models.Tag;

public interface TagRepository extends MongoRepository<Tag, ObjectId> {

}
