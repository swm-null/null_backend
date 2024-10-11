package com.example.oatnote.domain.memotag.service.tag;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.oatnote.domain.memotag.service.tag.model.Tag;

public interface TagRepository extends MongoRepository<Tag, String> {

    Optional<Tag> findByIdAndUserId(String tagId, String userId);

    Page<Tag> findByIdInAndUserIdOrderByName(List<String> tagsIds, Pageable pageable, String userId);

    List<Tag> findByIdInAndUserIdOrderByName(List<String> tagIds, String userIdt);

    void deleteByUserId(String userId);
}
