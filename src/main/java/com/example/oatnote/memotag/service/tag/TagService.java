package com.example.oatnote.memotag.service.tag;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.oatnote.memotag.service.tag.edge.TagEdgeService;
import com.example.oatnote.memotag.service.tag.edge.model.TagEdge;
import com.example.oatnote.memotag.service.tag.exception.TagNotFoundException;
import com.example.oatnote.memotag.service.tag.model.Tag;
import com.example.oatnote.memotag.service.tag.relation.TagsRelationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagEdgeService tagEdgeService;
    private final TagsRelationService tagsRelationService;
    private final TagRepository tagRepository;

    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    public List<Tag> getTags(List<String> tagIds, String userId) {
        return tagRepository.findByIdInAndUserId(tagIds, userId);
    }

    public Page<Tag> getPagedTags(List<String> tagsIds, PageRequest pageRequest, String userId) {
        return tagRepository.findByIdInAndUserId(tagsIds, pageRequest, userId);
    }

    public Tag getTag(String tagId, String userId) {
        return tagRepository.findByIdAndUserId(tagId, userId)
            .orElseThrow(() -> new TagNotFoundException("태그를 찾지 못했습니다: " + tagId));
    }

    public void deleteTag(Tag tag) {
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

    public void deleteRelation(String parentTagId, String childTagId) {
        tagsRelationService.deleteRelation(parentTagId, childTagId);
    }

    public void deleteUserAllData(String userId) {
        tagEdgeService.deleteUserAllData(userId);
        tagsRelationService.deleteUserAllData(userId);
        tagRepository.deleteByUserId(userId);
    }
}
