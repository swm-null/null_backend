package com.example.oatnote.domain.memotag.service.tag;

import static com.example.oatnote.domain.memotag.service.client.dto.AICreateStructureResponse.NewTag;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.oatnote.domain.memotag.service.client.dto.AICreateStructureResponse;
import com.example.oatnote.domain.memotag.service.tag.edge.TagEdgeService;
import com.example.oatnote.domain.memotag.service.tag.edge.model.TagEdge;
import com.example.oatnote.domain.memotag.service.tag.model.Tag;
import com.example.oatnote.web.exception.client.OatDataNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagService {

    private final TagEdgeService tagEdgeService;
    private final TagRepository tagRepository;

    public void createTag(Tag tag) {
        log.info("태그 생성 - 태그: {} / 유저: {}", tag.getId(), tag.getUserId());
        tagRepository.insert(tag);
    }

    public Tag getTag(String tagId, String userId) {
        return tagRepository.findByIdAndUserId(tagId, userId)
            .orElseThrow(() -> OatDataNotFoundException.withDetail("태그를 찾지 못했습니다.", tagId));
    }

    public List<Tag> getTags(List<String> tagIds, String userId) {
        return tagRepository.findByIdInAndUserIdOrderByUpdatedAtDesc(tagIds, userId);
    }

    public List<Tag> getTags(Set<String> tagIds, String userId) {
        return tagRepository.findByIdInAndUserIdOrderByUpdatedAtDesc(tagIds, userId);
    }

    public Page<Tag> getTags(List<String> tagIds, String userId, Pageable pageable) {
        return tagRepository.findByIdInAndUserId(tagIds, userId, pageable);
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

    public List<String> getParentTagsIds(String childTagId, String userId) {
        return tagEdgeService.getParentTagsIds(childTagId, userId);
    }

    public void deleteUserAllData(String userId) {
        log.info("태그 전체 삭제 - 유저: {}", userId);
        tagRepository.deleteByUserId(userId);
        tagEdgeService.deleteUserAllData(userId);
    }

    public void processTags(AICreateStructureResponse aiCreateStructureResponse, String userId, LocalDateTime time) {
        for (NewTag newTag : aiCreateStructureResponse.newTags()) {
            Tag tag = newTag.toTag(userId, time);
            createTag(tag);
        }

        tagEdgeService.createTagEdge(
            TagEdge.of(
                userId,
                aiCreateStructureResponse.newStructure(),
                aiCreateStructureResponse.newReversedStructure()
            )
        );
    }

    public TagEdge getTagEdge(String userId) {
        return tagEdgeService.getTagEdge(userId);
    }
}
