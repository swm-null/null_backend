package com.example.oatnote.memoTag.service.relation.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Document(collection = "memo_tag_relations")
public class MemoTagRelation {

    @Id
    private String id;

    @Indexed
    @Field("mid")
    @NotBlank(message = "메모 아이디는 비워둘 수 없습니다.")
    private String memoId;

    @Indexed
    @Field("tid")
    @NotBlank(message = "태그 아이디는 비워둘 수 없습니다.")
    private String tagId;
}
