package com.example.oatnote.memoTag.dto.innerDto;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(SnakeCaseStrategy.class)
public record PagedTagResponse(
    @Schema(description = "태그 이름", example = "학습", requiredMode = REQUIRED)
    String tag,

    @Schema(description = "해당 태그의 전체 메모 수", example = "30", requiredMode = REQUIRED)
    Long totalCount,

    @Schema(description = "현재 페이지에서 조회된 해당 태그의 메모 수", example = "5", requiredMode = REQUIRED)
    Integer currentCount,

    @Schema(description = "해당 태그의 전체 페이지 수", example = "3", requiredMode = REQUIRED)
    Integer totalPage,

    @Schema(description = "해당 태그의 현재 페이지", example = "1", requiredMode = REQUIRED)
    Integer currentPage,

    @Schema(description = "해당 페이지의 메모 리스트", requiredMode = REQUIRED)
    List<MemoTagsResponse> pagedMemoList
) {

}
