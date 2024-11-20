package com.example.oatnote.domain.memotag.service.tag;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.oatnote.domain.memotag.service.client.dto.AiCreateStructureResponse;
import com.example.oatnote.domain.memotag.service.tag.edge.TagEdgeService;
import com.example.oatnote.domain.memotag.service.tag.edge.model.TagEdge;
import com.example.oatnote.domain.memotag.service.tag.model.Tag;
import com.example.oatnote.web.controller.exception.client.OatDataNotFoundException;
import com.example.oatnote.web.controller.exception.client.OatIllegalArgumentException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagService {

    private final TagEdgeService tagEdgeService;
    private final TagRepository tagRepository;

    public Tag createTag(Tag tag, String userId) {
        log.info("태그 생성 {} / 유저: {}", tag.getId(), userId);
        return tagRepository.insert(tag);
    }

    public Tag createChildTag(String tagId, Tag childTag, String userId) {
        TagEdge tagEdge = tagEdgeService.getTagEdge(userId);
        Map<String, List<String>> tagEdges = tagEdge.getEdges();
        Map<String, List<String>> reversedTagEdges = tagEdge.getReversedEdges();

        tagEdges.putIfAbsent(tagId, new ArrayList<>());
        tagEdges.get(tagId).add(childTag.getId());

        reversedTagEdges.put(childTag.getId(), List.of(tagId));
        tagEdgeService.updateTagEdge(tagEdge, userId);

        return createTag(childTag, userId);
    }


    public void createTags(List<Tag> tags, String userId) {
        log.info("태그 리스트 생성 {} / 유저: {}", tags.stream().map(Tag::getId).toList(), userId);
        tagRepository.insert(tags);
    }

    public Tag getTag(String tagId, String userId) {
        return tagRepository.findByIdAndUserId(tagId, userId)
            .orElseThrow(() -> OatDataNotFoundException.withDetail("태그를 찾지 못했습니다.", tagId));
    }

    public List<Tag> getTags(List<String> tagIds, String userId) {
        return tagRepository.findByIdInAndUserIdOrderByUpdatedAtDesc(tagIds, userId);
    }

    public Page<Tag> getTags(List<String> tagIds, String userId, Pageable pageable) {
        return tagRepository.findByIdInAndUserId(tagIds, userId, pageable);
    }

    public Tag updateTag(Tag tag) {
        log.info("태그 업데이트 / 태그: {} / 유저: {}", tag.getId(), tag.getUserId());
        tagRepository.findByIdAndUserId(tag.getId(), tag.getUserId())
            .orElseThrow(() -> OatDataNotFoundException.withDetail("태그를 찾지 못했습니다.", tag.getId()));
        return tagRepository.save(tag);
    }

    public void deleteTags(Set<String> tagIds, String userId) {
        log.info("태그 리스트 삭제 / 태그: {} / 유저: {}", tagIds, userId);
        tagRepository.deleteByIdInAndUserId(tagIds, userId);
    }

    public void deleteUserAllData(String userId) {
        log.info("태그 전체 삭제 / 유저: {}", userId);
        tagRepository.deleteByUserId(userId);
        tagEdgeService.deleteUserAllData(userId);
    }

    public void processTags(AiCreateStructureResponse aiCreateStructureResponse, String userId) {
        List<Tag> tags = aiCreateStructureResponse.newTags().stream()
            .map(newTag -> newTag.toTag(userId))
            .toList();
        createTags(tags, userId);

        TagEdge tagEdge = TagEdge.of(
            aiCreateStructureResponse.newStructure(),
            aiCreateStructureResponse.newReversedStructure(),
            userId
        );
        tagEdgeService.createTagEdge(tagEdge);
    }

    public TagEdge getTagEdge(String userId) {
        return tagEdgeService.getTagEdge(userId);
    }

    public void updateTagEdge(TagEdge tagEdge, String userId) {
        tagEdgeService.updateTagEdge(tagEdge, userId);
    }

    public void createDefaultTagStructureForNewUser(String rootTagName, String userId) {
        Tag tag = Tag.of(
            rootTagName,
            userId
        );
        createTag(tag, userId);

        TagEdge tagEdge = TagEdge.of(
            Map.of(tag.getId(), List.of()),
            Map.of(tag.getId(), List.of()),
            userId
        );
        tagEdgeService.createTagEdge(tagEdge);
    }

    public List<Tag> getAncestorTags(String tagId, String userId) {
        Map<String, List<String>> reversedTagEdges = tagEdgeService.getTagEdge(userId).getReversedEdges();
        List<Tag> ancestorTags = new LinkedList<>();
        getAncestorTagsUsingDFS(tagId, userId, reversedTagEdges, ancestorTags, new HashSet<>());
        return ancestorTags;
    }

    public List<Tag> getChildTags(String tagId, String userId) {
        Map<String, List<String>> tagEdges = tagEdgeService.getTagEdge(userId).getEdges();
        List<String> childTagIds = tagEdges.getOrDefault(tagId, List.of());
        return getTags(childTagIds, userId);
    }

    void getAncestorTagsUsingDFS(
        String currentTagId,
        String userId,
        Map<String, List<String>> reversedTagEdges,
        List<Tag> ancestorTags,
        Set<String> visited
    ) {
        if (visited.contains(currentTagId) || currentTagId.equals(userId)) {
            return;
        }
        visited.add(currentTagId);

        Tag currentTag = getTag(currentTagId, userId);
        ancestorTags.add(0, currentTag);

        List<String> parentTagIds = reversedTagEdges.getOrDefault(currentTagId, List.of());
        getAncestorTagsUsingDFS(parentTagIds.get(0), userId, reversedTagEdges, ancestorTags, visited);
    }

    public void validateTagExist(String name, String userId) {
        if(tagRepository.existsByNameAndUserId(name, userId)) {
            throw OatIllegalArgumentException.withDetail("이미 존재하는 태그입니다.", name);
        }
    }
}
