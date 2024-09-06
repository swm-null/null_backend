package com.example.oatnote.memoTag.service.tagsRelation;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.oatnote.memoTag.service.tagsRelation.model.TagsRelation;

public interface TagsRelationRepository extends MongoRepository<TagsRelation, UUID> {

    void deleteByParentTagIdAndChildTagId(UUID parentTagId, UUID childTagId);

    List<TagsRelation> findByChildTagId(UUID parentTagId);

    List<TagsRelation> findByParentTagId(UUID parentTagId);

    Integer countByParentTagId(UUID parentTagId);
}
