package com.example.oatnote.memoTag.service.tagsRelation;

import org.springframework.stereotype.Service;

import com.example.oatnote.memoTag.service.tagsRelation.exception.TagsRelationNotFoundException;
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

    public String getParentTagId(String parentTagId) {
        return tagsRelationRepository.findByChildTagId(parentTagId)
            .orElseThrow(() -> new TagsRelationNotFoundException("태그 릴레이션을 찾지 못했습니다. childTagId :" + parentTagId))
            .getParentTagId();
    }

}
