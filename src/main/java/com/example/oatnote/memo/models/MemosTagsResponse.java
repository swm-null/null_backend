package com.example.oatnote.memo.models;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import java.util.List;

import com.example.oatnote.memo.models.InnerResponse.MemoTagsResponse;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(SnakeCaseStrategy.class)
public record MemosTagsResponse(
    @Schema(description = "특정 태그의 총 메모의 수", example = "57", requiredMode = REQUIRED)
    Long totalCount,

    @Schema(description = "특정 태그의 메모 중에 현재 페이지에서 조회된 수", example = "10", requiredMode = REQUIRED)
    Integer currentCount,

    @Schema(description = "특정 태그의 메모들을 조회할 수 있는 최대 페이지", example = "6", requiredMode = REQUIRED)
    Integer totalPage,

    @Schema(description = "현재 페이지", example = "2", requiredMode = REQUIRED)
    Integer currentPage,

    @Schema(description = "메모 리스트", requiredMode = REQUIRED)
    List<MemoTagsResponse> memoTags
) {

    public static MemosTagsResponse of(
        Long totalCount,
        Integer currentCount,
        Integer totalPage,
        Integer currentPage,
        List<MemoTagsResponse> memoTags
    ) {
        return new MemosTagsResponse(
            totalCount,
            currentCount,
            totalPage,
            currentPage,
            memoTags
        );
    }
}
