package com.example.oatnote.memo.service.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
@Document(collection = "memo_tag_relations")
public class MemoTagRelation {

    @Id
    private String id;

    @NotBlank
    @Indexed
    private String memoId;

    @NotBlank
    @Indexed
    private String tagId;
}
