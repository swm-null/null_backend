package com.example.oatnote.memoTag.models;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(SnakeCaseStrategy.class)
public record RootMemosTagsResponse(
    @Schema(description = "태그 리스트", example = "[\"학습\", \"일정\"]")
    List<String> tags,

    @Schema(description = "특정 태그의 총 메모의 수", example = "57", requiredMode = REQUIRED)
    Long totalCount,

    @Schema(description = "특정 태그의 메모 중에 현재 페이지에서 조회된 수", example = "10", requiredMode = REQUIRED)
    Integer currentCount,

    @Schema(description = "특정 태그의 메모들을 조회할 수 있는 최대 페이지", example = "6", requiredMode = REQUIRED)
    Integer totalPage,

    @Schema(description = "현재 페이지", example = "2", requiredMode = REQUIRED)
    Integer currentPage,

    @Schema(description = "태그별 메모 리스트", requiredMode = REQUIRED)
    List<MemosTagsResponse> memosTags
) {

    public static RootMemosTagsResponse of(
        List<String> tags,
        Long totalCount,
        Integer currentCount,
        Integer totalPage,
        Integer currentPage,
        List<MemosTagsResponse> memosTags
    ) {
        return new RootMemosTagsResponse(
            tags,
            totalCount,
            currentCount,
            totalPage,
            currentPage,
            memosTags
        );
    }
}
