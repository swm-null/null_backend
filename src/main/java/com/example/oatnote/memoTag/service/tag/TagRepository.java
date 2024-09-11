package com.example.oatnote.memoTag.service.tag;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.oatnote.memoTag.service.tag.model.Tag;

public interface TagRepository extends MongoRepository<Tag, String> {

    Optional<Tag> findByIdAndUserId(String tagId, String userId);

    Page<Tag> findByIdInAndUserId(List<String> tagsIds, PageRequest pageRequest, String userId);

    Optional<Tag> findByNameAndUserId(String parentTagName, String userId);

    List<Tag> findByIdInAndUserId(List<String> tagIds, String userId);
}
