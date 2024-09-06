package com.example.oatnote.memoTag.service.tag;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    public List<Tag> getTags(List<UUID> childTagsIds) {
        return tagRepository.findAllById(childTagsIds);
    }

    public List<Tag> getPagedTags(List<UUID> tagIds) {
        return tagRepository.findAllById(tagIds);
    }

    public Page<Tag> getPagedTags(PageRequest pageRequest) {
        return tagRepository.findAll(pageRequest);
    }

    public Tag getTag(UUID tagId) {
        return tagRepository.findById(tagId)
            .orElseThrow(() -> new TagNotFoundException("태그를 찾지 못했습니다: " + tagId));
    }

    public void deleteTag(Tag tag) {
        tagRepository.delete(tag);
    }
}
