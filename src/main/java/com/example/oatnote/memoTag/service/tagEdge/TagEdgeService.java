package com.example.oatnote.memoTag.service.tagEdge;

import org.springframework.stereotype.Service;

import com.example.oatnote.memoTag.service.tagEdge.model.TagEdge;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagEdgeService {

    private final TagEdgeRepository tagEdgeRepository;

    public void saveTagEdge(TagEdge tagEdge) {
        tagEdgeRepository.save(tagEdge);
    }
}
