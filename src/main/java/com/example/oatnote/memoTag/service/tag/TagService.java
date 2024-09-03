package com.example.oatnote.memoTag.service.tag;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.oatnote.memoTag.service.tag.exception.TagNotFoundException;
import com.example.oatnote.memoTag.service.tag.model.Tag;

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

    public List<Tag> getRootTags() {
        return tagRepository.findByParentTagIdIsNull();
    }

    public List<Tag> getTags(List<String> tagIds) {
        return tagRepository.findAllById(tagIds);
    }

    public Tag getTag(String tagId) {
        return tagRepository.findById(tagId)
            .orElseThrow(() -> new TagNotFoundException("태그를 찾지 못했습니다: " + tagId));
    }

    public void deleteTag(Tag tag) {
        tagRepository.delete(tag);
    }
}
