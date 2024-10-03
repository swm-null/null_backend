package com.example.oatnote.memotag.service.tag.relation;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.oatnote.memotag.service.tag.relation.model.TagsRelation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagsRelationService {

    private final TagsRelationRepository tagsRelationRepository;

    public void createRelation(String parentTagId, String childTagId, String userId) {
        log.info("태그 릴레이션 생성 - 부모 태그: {} / 자식 태그: {} / 유저: {}", parentTagId, childTagId, userId);
        TagsRelation tagsRelation = new TagsRelation(parentTagId, childTagId, userId);
        tagsRelationRepository.insert(tagsRelation);
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

    public void deleteRelation(String parentTagId, String childTagId, String userId) {
        log.info("태그 릴레이션 삭제 - 부모 태그: {} / 자식 태그: {} / 유저: {}", parentTagId, childTagId, userId);
        tagsRelationRepository.deleteByParentTagIdAndChildTagIdAndUserId(parentTagId, childTagId, userId);
    }

    public void deleteUserAllData(String userId) {
        log.info("태그 릴레이션 전체 삭제 - 유저: {}", userId);
        tagsRelationRepository.deleteByUserId(userId);
    }
}
