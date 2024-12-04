package com.example.oatnote.memotag.service.tag;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.oatnote.memotag.service.tag.model.Tag;

public interface TagRepository extends MongoRepository<Tag, String> {

    Optional<Tag> findByIdAndUserId(String tagId, String userId);

    Page<Tag> findByIdInAndUserId(List<String> tagIds, String userId, Pageable pageable);

    List<Tag> findByIdInAndUserId(List<String> tagIds, String userId, Sort sort);

    void deleteByIdInAndUserId(Set<String> tagIds, String userId);

    void deleteByUserId(String userId);

    boolean existsByNameAndUserId(String name, String userId);
}
