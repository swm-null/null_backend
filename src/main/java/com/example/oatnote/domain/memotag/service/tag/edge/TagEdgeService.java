package com.example.oatnote.domain.memotag.service.tag.edge;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.oatnote.domain.memotag.service.tag.edge.model.TagEdge;
import com.example.oatnote.web.exception.client.OatDataNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagEdgeService {

    private final TagEdgeRepository tagEdgeRepository;

    public void createTagEdge(TagEdge tagEdge) {
        log.info("태그 엣지 생성 - 유저: {}", tagEdge.getUserId());
        if (tagEdgeRepository.findByUserId(tagEdge.getUserId()).isPresent()) {
            tagEdgeRepository.deleteByUserId(tagEdge.getUserId());
        }
        tagEdgeRepository.insert(tagEdge);
    }

    public List<String> getParentTagsIds(String tagId, String userId) {
        Map<String, List<String>> reversedEdges = getTagEdge(userId).getReversedEdges();
        return reversedEdges.get(tagId);
    }

    public TagEdge getTagEdge(String userId) {
        return tagEdgeRepository.findByUserId(userId)
            .orElseThrow(() -> OatDataNotFoundException.withDetail("태그 엣지를 찾지 못했습니다", userId));
    }

    public void deleteUserAllData(String userId) {
        log.info("태그 엣지 전체 삭제 - 유저: {}", userId);
        tagEdgeRepository.deleteByUserId(userId);
    }

    public void updateTagEdge(TagEdge tagEdge, String userId) {
        log.info("태그 엣지 업데이트 - id : {} / 유저: {}", tagEdge.getId(), userId);
        tagEdgeRepository.save(tagEdge);
    }
}
