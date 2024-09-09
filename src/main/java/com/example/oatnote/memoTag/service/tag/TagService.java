package com.example.oatnote.memoTag.service.tag;

import java.util.List;

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

    public List<Tag> getTags(List<String> tagIds, String userId) {
        return tagRepository.findByIdInAndUserId(tagIds, userId);
    }

    public Page<Tag> getPagedTags(List<String> tagsIds, PageRequest pageRequest, String userId) {
        return tagRepository.findByIdInAndUserId(tagsIds, pageRequest, userId);
    }

    public Tag getTag(String tagId, String userId) {
        return tagRepository.findByIdAndUserId(tagId, userId)
            .orElseThrow(() -> new TagNotFoundException("태그를 찾지 못했습니다: " + tagId));
    }

    public Tag getTagByName(String parentTagName, String userId) {
        return tagRepository.findByNameAndUserId(parentTagName, userId)
            .orElseThrow(() -> new TagNotFoundException("태그를 찾지 못했습니다: " + parentTagName));
    }

    public void deleteTag(Tag tag) {
        tagRepository.delete(tag);
    }
}
