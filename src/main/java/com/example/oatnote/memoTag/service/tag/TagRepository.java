package com.example.oatnote.memoTag.service.tag;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.oatnote.memoTag.service.tag.model.Tag;

public interface TagRepository extends MongoRepository<Tag, UUID> {

    Page<Tag> findAllByIdIn(List<UUID> tagsIds, PageRequest pageRequest);
}
