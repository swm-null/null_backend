package com.example.oatnote.domain.memotag.service.tag;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.oatnote.domain.memotag.service.aiClient.dto.AiCreateStructureResponse;
import com.example.oatnote.domain.memotag.service.tag.edge.TagEdgeService;
import com.example.oatnote.domain.memotag.service.tag.edge.model.TagEdge;
import com.example.oatnote.domain.memotag.service.tag.model.Tag;
import com.example.oatnote.web.controller.exception.client.OatDataNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagService {

    private final TagEdgeService tagEdgeService;
    private final TagRepository tagRepository;

    public void createTag(Tag tag) {
        log.info("태그 생성 / ID: {} / 유저: {}", tag.getId(), tag.getUserId());
        tagRepository.insert(tag);
    }

    public void createTags(List<Tag> tags, String userId) {
        log.info("태그 리스트 생성 / IDs: {} / 유저: {}", tags.stream().map(Tag::getId).toList(), userId);
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
        createTag(tag);

        TagEdge tagEdge = TagEdge.of(
            Map.of(tag.getId(), List.of()),
            Map.of(tag.getId(), List.of()),
            userId
        );
        tagEdgeService.createTagEdge(tagEdge);
    }

    public List<Tag> getChildTags(String tagId, String userId) {
        Map<String, List<String>> tagEdges = tagEdgeService.getTagEdge(userId).getEdges();
        List<String> childTagIds = tagEdges.getOrDefault(tagId, List.of());
        return getTags(childTagIds, userId);
    }
}
