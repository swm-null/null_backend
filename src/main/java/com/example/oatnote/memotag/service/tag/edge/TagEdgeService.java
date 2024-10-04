package com.example.oatnote.memotag.service.tag.edge;

import org.springframework.stereotype.Service;

import com.example.oatnote.memotag.service.tag.edge.model.TagEdge;

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

    public void deleteUserAllData(String userId) {
        log.info("태그 엣지 전체 삭제 - 유저: {}", userId);
        tagEdgeRepository.deleteByUserId(userId);
    }
}
