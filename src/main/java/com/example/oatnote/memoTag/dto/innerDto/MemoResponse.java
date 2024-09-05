package com.example.oatnote.memoTag.dto.innerDto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.oatnote.memoTag.service.memo.model.Memo;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(SnakeCaseStrategy.class)
public record MemoResponse(
    @Schema(description = "메모 고유 ID", example = "61b72b3e9b1e8b1e4c8b4560")
    String id,

    @Schema(description = "내용", example = "내일은 5시 멘토링을 들어야해")
    String content,

    @Schema(
        description = "이미지 URL 리스트",
        example = "[\"https://oatnote.com/image1.jpg\", \"https://oatnote.com/image2.jpg\"]"
    )
    List<String> imageUrls,

    @Schema(description = "생성일시", example = "2024-08-21T03:47:23.328108")
    LocalDateTime createdAt,

    @Schema(description = "수정일시", example = "2024-08-21T03:47:23.328108")
    LocalDateTime updatedAt
) {

    public static MemoResponse from(Memo memo) {
        return new MemoResponse(
            memo.getId(),
            memo.getContent(),
            memo.getImageUrls(),
            memo.getCreatedAt(),
            memo.getUpdatedAt()
        );
    }
}
