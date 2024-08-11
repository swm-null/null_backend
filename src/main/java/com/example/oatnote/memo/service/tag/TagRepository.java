package com.example.oatnote.memo.service.tag;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.oatnote.memo.service.tag.models.Tag;

public interface TagRepository extends MongoRepository<Tag, String> {

    List<Tag> findByParentTagIdIsNull();
}
