package com.example.memo.memo.service;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.memo.memo.models.TagResponse;
import com.example.memo.memo.service.exception.MemoNotFoundException;
import com.example.memo.memo.service.models.Tag;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }


    public List<Tag> getTagsByDepth(int depth) {
        return tagRepository.findByDepth(depth);
    }

    public List<Tag> getTagsById(List<String> tagIds) {
        return tagRepository.findAllById(tagIds);
    }

    public Tag getTagById(String tagId) {
        return tagRepository.findById(tagId)
            .orElseThrow(() -> new MemoNotFoundException("태그를 찾지 못했습니다: " + tagId));
    }

    public void deleteTag(Tag tag) {
        tagRepository.delete(tag);
    }
}
