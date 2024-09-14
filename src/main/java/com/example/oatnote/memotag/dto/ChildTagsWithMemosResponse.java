package com.example.oatnote.memotag.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.oatnote.memotag.dto.innerDto.TagResponse;
import com.example.oatnote.web.models.Criteria;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(SnakeCaseStrategy.class)
public record ChildTagsWithMemosResponse(
    @Schema(description = "특정 태그의 총 메모의 수", example = "57", requiredMode = REQUIRED)
    Long totalCount,

    @Schema(description = "특정 태그의 메모 중에 현재 페이지에서 조회된 수", example = "10", requiredMode = REQUIRED)
    Integer currentCount,

    @Schema(description = "특정 태그의 메모들을 조회할 수 있는 최대 페이지", example = "6", requiredMode = REQUIRED)
    Integer totalPage,

    @Schema(description = "현재 페이지", example = "2", requiredMode = REQUIRED)
    Integer currentPage,

    @Schema(description = "페이징된 자식 태그 리스트", requiredMode = REQUIRED)
    List<TagResponse> tags,

    @Schema(description = "페이징된 태그별 메모 리스트", requiredMode = REQUIRED)
    List<MemosResponse> memos
) {

    public static ChildTagsWithMemosResponse from(
        Page<TagResponse> pagedTagResult,
        Criteria tagCriteria,
        Page<MemosResponse> pagedMemoResult
    ) {
        return new ChildTagsWithMemosResponse(
            pagedTagResult.getTotalElements(),
            pagedTagResult.getContent().size(),
            pagedTagResult.getTotalPages(),
            tagCriteria.getPage() + 1,
            pagedTagResult.getContent(),
            pagedMemoResult.getContent()
        );
    }
}
