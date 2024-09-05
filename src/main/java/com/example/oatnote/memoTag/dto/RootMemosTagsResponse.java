package com.example.oatnote.memoTag.dto;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.oatnote.memoTag.dto.innerDto.TagResponse;
import com.example.oatnote.memoTag.service.tag.model.Tag;
import com.example.oatnote.web.models.Criteria;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(SnakeCaseStrategy.class)
public record RootMemosTagsResponse(
    @Schema(description = "태그 리스트", example = "[\"학습\", \"일정\"]")
    List<TagResponse> tags,

    @Schema(description = "전체 메모의 수", example = "57", requiredMode = REQUIRED)
    Long totalCount,

    @Schema(description = "현재 페이지에서 조회된 메모 수", example = "10", requiredMode = REQUIRED)
    Integer currentCount,

    @Schema(description = "전체 페이지 수", example = "6", requiredMode = REQUIRED)
    Integer totalPage,

    @Schema(description = "현재 페이지", example = "2", requiredMode = REQUIRED)
    Integer currentPage,

    @Schema(description = "태그별 메모태그 리스트", requiredMode = REQUIRED)
    List<PagedMemosTagsResponse> pagedMemosTags
) {

    public static RootMemosTagsResponse from(
        List<Tag> tags,
        Page<PagedMemosTagsResponse> pagedResult,
        Criteria criteria
    ) {
        return new RootMemosTagsResponse(
            tags.stream().map(TagResponse::from).toList(),
            pagedResult.getTotalElements(),
            pagedResult.getContent().size(),
            pagedResult.getTotalPages(),
            criteria.getPage() + 1,
            pagedResult.getContent()
        );
    }
}
