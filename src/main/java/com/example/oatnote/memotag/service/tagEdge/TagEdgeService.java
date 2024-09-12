package com.example.oatnote.memotag.service.tagEdge;

import org.springframework.stereotype.Service;

import com.example.oatnote.memotag.service.tagEdge.model.TagEdge;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagEdgeService {

    private final TagEdgeRepository tagEdgeRepository;

    public void saveTagEdge(TagEdge tagEdge) {
        if (tagEdgeRepository.findByUserId(tagEdge.getUserId()).isPresent()) {
            tagEdgeRepository.deleteByUserId(tagEdge.getUserId());
        }
        tagEdgeRepository.save(tagEdge);
    }
}
