package com.example.oatnote.memotag.service.tag.relation;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.oatnote.memotag.service.tag.relation.model.TagsRelation;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagsRelationService {

    private final TagsRelationRepository tagsRelationRepository;

    public void createRelation(String parentTagId, String childTagId) {
        TagsRelation tagsRelation = new TagsRelation(parentTagId, childTagId);
        tagsRelationRepository.insert(tagsRelation);
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
}
