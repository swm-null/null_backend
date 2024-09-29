package com.example.oatnote.memotag.service.tag.edge;

import org.springframework.stereotype.Service;

import com.example.oatnote.memotag.service.tag.edge.model.TagEdge;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagEdgeService {

    private final TagEdgeRepository tagEdgeRepository;

    public void createTagEdge(TagEdge tagEdge) {
        if (tagEdgeRepository.findByUserId(tagEdge.getUserId()).isPresent()) {
            tagEdgeRepository.deleteByUserId(tagEdge.getUserId());
        }
        tagEdgeRepository.insert(tagEdge);
    }

    public void deleteUserAllData(String userId) {
        tagEdgeRepository.deleteByUserId(userId);
    }
}
