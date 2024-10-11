package com.example.oatnote.domain.memotag.dto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.oatnote.web.model.Criteria;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(SnakeCaseStrategy.class)
public record ChildTagsWithMemosResponse(
    @Schema(description = "태그별 메모 리스트")
    TagWithMemosResponse tagWithMemos,

    @Schema(description = "특정 태그의 총 자식 태그의 수", example = "57", requiredMode = REQUIRED)
    Long totalCount,

    @Schema(description = "특정 태그의 자식 태그 중에 현재 페이지에서 조회된 수", example = "10", requiredMode = REQUIRED)
    Integer currentCount,

    @Schema(description = "특정 태그의 자식 태그들을 조회할 수 있는 최대 페이지", example = "6", requiredMode = REQUIRED)
    Integer totalPage,

    @Schema(description = "현재 페이지", example = "2", requiredMode = REQUIRED)
    Integer currentPage,

    @Schema(description = "현재 페이지의 자식 태그별 메모 리스트", requiredMode = REQUIRED)
    List<TagWithMemosResponse> childTagsWithMemos
) {

    public static ChildTagsWithMemosResponse from(
        TagWithMemosResponse tagWithMemosResponse,
        Page<TagWithMemosResponse> pageResult,
        Criteria Criteria
    ) {
        return new ChildTagsWithMemosResponse(
            tagWithMemosResponse,
            pageResult.getTotalElements(),
            pageResult.getContent().size(),
            pageResult.getTotalPages(),
            Criteria.getPage() + 1,
            pageResult.getContent()
        );
    }
}
