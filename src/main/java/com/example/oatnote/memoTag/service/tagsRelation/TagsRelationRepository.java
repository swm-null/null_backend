package com.example.oatnote.memoTag.service.tagsRelation;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.oatnote.memoTag.service.tagsRelation.model.TagsRelation;

public interface TagsRelationRepository extends MongoRepository<TagsRelation, String> {

    void deleteByParentTagIdAndChildTagId(String parentTagId, String childTagId);

    Optional<TagsRelation> findByChildTagId(String parentTagId);
}
