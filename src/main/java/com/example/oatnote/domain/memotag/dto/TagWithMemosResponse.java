package com.example.oatnote.domain.memotag.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.oatnote.domain.memotag.dto.innerDto.MemoResponse;
import com.example.oatnote.domain.memotag.dto.innerDto.TagResponse;
import com.example.oatnote.domain.memotag.service.tag.model.Tag;
import com.example.oatnote.web.model.Criteria;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(SnakeCaseStrategy.class)
public record TagWithMemosResponse(
    @Schema(description = "자식 태그")
    TagResponse tag,

    @Schema(description = "자식 태그의 자식 태그 리스트")
    List<TagResponse> childTags,

    @Schema(description = "특정 태그와 연관된 총 메모의 수", example = "57", requiredMode = REQUIRED)
    Long totalCount,

    @Schema(description = "특정 태그와 연관된 메모 중에 현재 페이지에서 조회된 수", example = "10", requiredMode = REQUIRED)
    Integer currentCount,

    @Schema(description = "특정 태그와 연관된 메모들을 조회할 수 있는 최대 페이지", example = "6", requiredMode = REQUIRED)
    Integer totalPage,

    @Schema(description = "현재 페이지", example = "2", requiredMode = REQUIRED)
    Integer currentPage,

    @Schema(description = "현재 페이지의 메모 리스트", requiredMode = REQUIRED)
    List<MemoResponse> memos
) {

    public static TagWithMemosResponse from(
        Tag tag,
        List<TagResponse> childTagsResponse,
        Page<MemoResponse> pageResult,
        Criteria criteria
    ) {
        return new TagWithMemosResponse(
            TagResponse.fromTag(tag),
            childTagsResponse,
            pageResult.getTotalElements(),
            pageResult.getContent().size(),
            pageResult.getTotalPages(),
            criteria.getCurrentPage(),
            pageResult.getContent()
        );
    }
}
