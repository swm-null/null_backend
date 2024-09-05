package com.example.oatnote.memoTag.service.tagsRelation;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.oatnote.memoTag.service.tagsRelation.model.TagsRelation;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagsRelationService {

    private final TagsRelationRepository tagsRelationRepository;

    public void createRelation(String parentTagId, String childTagId) {
        TagsRelation tagRelation = TagsRelation.builder()
            .parentTagId(parentTagId)
            .childTagId(childTagId)
            .build();
        tagsRelationRepository.insert(tagRelation);
    }

    public void deleteRelation(String parentTagId, String childTagId) {
        tagsRelationRepository.deleteByParentTagIdAndChildTagId(parentTagId, childTagId);
    }

    public List<String> getParentTagsIds(String childTagId) {
        return tagsRelationRepository.findByChildTagId(childTagId).stream()
            .map(TagsRelation::getParentTagId)
            .toList();
    }

    public List<String> getChildTagsIds(String parentTagId) {
        return tagsRelationRepository.findByParentTagId(parentTagId).stream()
            .map(TagsRelation::getChildTagId)
            .toList();
    }

    public Integer countChildTags(String parentTagId) {
        return tagsRelationRepository.countByParentTagId(parentTagId);
    }
}
