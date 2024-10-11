package com.example.oatnote.domain.memotag.service.tag.relation;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.oatnote.domain.memotag.service.tag.relation.model.TagsRelation;

public interface TagsRelationRepository extends MongoRepository<TagsRelation, String> {

    List<TagsRelation> findByParentTagIdAndUserId(String parentTagId, String userId);

    List<TagsRelation> findByChildTagIdAndUserId(String parentTagId, String userId);

    void deleteByParentTagIdAndChildTagIdAndUserId(String parentTagId, String childTagId, String userId);

    void deleteByUserId(String userId);
}
