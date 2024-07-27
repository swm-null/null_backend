package com.example.memo.memo.service;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.memo.memo.service.models.Tag;

public interface TagRepository extends MongoRepository<Tag, String> {

    List<Tag> findByDepth(int depth);
}
