package com.example.oatnote.memoTag.service.tagsRelation;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.oatnote.memoTag.service.tagsRelation.model.TagsRelation;

public interface TagsRelationRepository extends MongoRepository<TagsRelation, String> {

    void deleteByParentTagIdAndChildTagId(String parentTagId, String childTagId);

    List<TagsRelation> findByChildTagId(String parentTagId);

    List<TagsRelation> findByParentTagId(String parentTagId);

    Integer countByParentTagId(String parentTagId);
}
