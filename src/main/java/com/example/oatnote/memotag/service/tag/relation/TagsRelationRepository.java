package com.example.oatnote.memotag.service.tag.relation;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.oatnote.memotag.service.tag.relation.model.TagsRelation;

public interface TagsRelationRepository extends MongoRepository<TagsRelation, String> {

    List<TagsRelation> findByParentTagId(String parentTagId);

    List<TagsRelation> findByChildTagId(String parentTagId);

    void deleteByParentTagIdAndChildTagIdAndUserId(String parentTagId, String childTagId, String userId);

    void deleteByUserId(String userId);
}
