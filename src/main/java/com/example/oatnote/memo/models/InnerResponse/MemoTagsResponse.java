package com.example.oatnote.memo.models.InnerResponse;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import java.time.LocalDateTime;
import java.util.List;

import com.example.oatnote.memo.service.memo.models.Memo;
import com.example.oatnote.memo.service.tag.models.Tag;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(SnakeCaseStrategy.class)
public record MemoTagsResponse(
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
    LocalDateTime updatedAt,

    @Schema(description = "태그 리스트")
    List<TagResponse> tags
) {

    public static MemoTagsResponse from(Memo memo, List<Tag> tags) {
        return new MemoTagsResponse(
            memo.getId(),
            memo.getContent(),
            memo.getImageUrls(),
            memo.getCreatedAt(),
            memo.getUpdatedAt(),
            tags.stream().map(TagResponse::from).toList()
        );
    }
}