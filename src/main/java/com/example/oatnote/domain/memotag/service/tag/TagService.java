package com.example.oatnote.domain.memotag.service.tag;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.oatnote.domain.memotag.service.tag.edge.TagEdgeService;
import com.example.oatnote.domain.memotag.service.tag.edge.model.TagEdge;
import com.example.oatnote.domain.memotag.service.tag.model.Tag;
import com.example.oatnote.domain.memotag.service.tag.relation.TagsRelationService;
import com.example.oatnote.web.exception.client.OatDataNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagService {

    private final TagEdgeService tagEdgeService;
    private final TagsRelationService tagsRelationService;
    private final TagRepository tagRepository;

    public void createTag(Tag tag) {
        log.info("태그 생성 - 태그: {} / 유저: {}", tag.getId(), tag.getUserId());
        tagRepository.insert(tag);
    }

    public List<Tag> getTags(List<String> tagIds, String userId) {
        return tagRepository.findByIdInAndUserIdOrderByName(tagIds, userId);
    }

    public Page<Tag> getPagedTags(List<String> tagsIds, PageRequest pageRequest, String userId) {
        return tagRepository.findByIdInAndUserId(tagsIds, pageRequest, userId);
    }

    public Tag getTag(String tagId, String userId) {
        return tagRepository.findByIdAndUserId(tagId, userId)
            .orElseThrow(() -> OatDataNotFoundException.withDetail("태그를 찾지 못했습니다.", tagId));
    }

    public Tag updateTag(Tag tag) {
        log.info("태그 업데이트 - 태그: {} / 유저: {}", tag.getId(), tag.getUserId());
        tagRepository.findByIdAndUserId(tag.getId(), tag.getUserId())
            .orElseThrow(() -> OatDataNotFoundException.withDetail("태그를 찾지 못했습니다.", tag.getId()));
        return tagRepository.save(tag);
    }

    public void deleteTag(Tag tag) {
        log.info("태그 삭제 - 태그: {} / 유저: {}", tag.getId(), tag.getUserId());
        tagRepository.delete(tag);
    }

    public void createTagEdge(TagEdge tagEdge) {
        tagEdgeService.createTagEdge(tagEdge);
    }

    public void createRelation(String parentTagId, String childTagId, String userId) {
        tagsRelationService.createRelation(parentTagId, childTagId, userId);
    }

    public List<String> getChildTagsIds(String parentTagId) {
        return tagsRelationService.getChildTagsIds(parentTagId);
    }

    public List<Tag> getChildTags(String parentTagId, String userId) {
        return getTags(getChildTagsIds(parentTagId), userId);
    }

    public List<String> getParentTagsIds(String childTagId) {
        return tagsRelationService.getParentTagsIds(childTagId);
    }

    public void deleteRelation(String parentTagId, String childTagId, String userId) {
        tagsRelationService.deleteRelation(parentTagId, childTagId, userId);
    }

    public void deleteUserAllData(String userId) {
        tagEdgeService.deleteUserAllData(userId);
        tagsRelationService.deleteUserAllData(userId);
        tagRepository.deleteByUserId(userId);
    }

    public Integer countChildTags(String tagId, String userId) {
        return getChildTagsIds(tagId).size();
    }

    public Page<Tag> getPagedChildTags(String tagId, PageRequest pageRequest, String userId) {
        return getPagedTags(getChildTagsIds(tagId), pageRequest, userId);
    }
}
