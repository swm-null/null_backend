package com.example.oatnote.memoTag.service.tagsRelation;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.oatnote.memoTag.service.tagsRelation.model.TagsRelation;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagsRelationService {

    private final TagsRelationRepository tagsRelationRepository;

    public void createRelation(UUID parentTagId, UUID childTagId) {
        TagsRelation tagsRelation = new TagsRelation(parentTagId, childTagId);
        tagsRelationRepository.insert(tagsRelation);
    }

    public void deleteRelation(UUID parentTagId, UUID childTagId) {
        tagsRelationRepository.deleteByParentTagIdAndChildTagId(parentTagId, childTagId);
    }

    public List<UUID> getParentTagsIds(UUID childTagId) {
        return tagsRelationRepository.findByChildTagId(childTagId).stream()
            .map(TagsRelation::getParentTagId)
            .toList();
    }

    public List<UUID> getChildTagsIds(UUID parentTagId) {
        return tagsRelationRepository.findByParentTagId(parentTagId).stream()
            .map(TagsRelation::getChildTagId)
            .toList();
    }

    public Integer countChildTags(UUID parentTagId) {
        return tagsRelationRepository.countByParentTagId(parentTagId);
    }
}
