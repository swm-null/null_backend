package com.example.oatnote.memoTag.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.oatnote.memoTag.dto.innerDto.MemoTagsResponse;
import com.example.oatnote.memoTag.dto.innerDto.TagResponse;
import com.example.oatnote.memoTag.service.tag.model.Tag;
import com.example.oatnote.web.models.Criteria;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(SnakeCaseStrategy.class)
public record PagedMemosTagsResponse(
    @Schema(description = "태그 이름", requiredMode = REQUIRED)
    TagResponse tag,

    @Schema(description = "특정 태그의 총 메모의 수", example = "57", requiredMode = REQUIRED)
    Long totalCount,

    @Schema(description = "특정 태그의 메모 중에 현재 페이지에서 조회된 수", example = "10", requiredMode = REQUIRED)
    Integer currentCount,

    @Schema(description = "특정 태그의 메모들을 조회할 수 있는 최대 페이지", example = "6", requiredMode = REQUIRED)
    Integer totalPage,

    @Schema(description = "현재 페이지", example = "2", requiredMode = REQUIRED)
    Integer currentPage,

    @Schema(description = "메모태그 리스트", requiredMode = REQUIRED)
    List<MemoTagsResponse> memoTags
) {

    public static PagedMemosTagsResponse from(
        Tag tag,
        Page<MemoTagsResponse> pagedResult,
        Criteria criteria
    ) {
        return new PagedMemosTagsResponse(
            TagResponse.from(tag),
            pagedResult.getTotalElements(),
            pagedResult.getContent().size(),
            pagedResult.getTotalPages(),
            criteria.getPage() + 1,
            pagedResult.getContent()
        );
    }
}
