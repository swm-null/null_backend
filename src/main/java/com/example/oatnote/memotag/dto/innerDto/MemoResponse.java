package com.example.oatnote.memotag.dto.innerDto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.oatnote.memotag.service.client.dto.innerDto.RawTag;
import com.example.oatnote.memotag.service.memo.model.Memo;
import com.example.oatnote.memotag.service.tag.model.Tag;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(SnakeCaseStrategy.class)
public record MemoResponse(
    @Schema(description = "메모 고유 ID", example = "61b72b3e9b1e8b1e4c8b4560")
    String id,

    @Schema(description = "내용", example = "내일은 5시 멘토링을 들어야해")
    String content,

    @Schema(description = "이미지 URL 리스트", example = "[\"https://oatnote.com/image1.jpg\"]")
    List<String> imageUrls,

    @Schema(description = "음성 URL 리스트", example = "[\"https://oatnote.com/voice1.mp3\"]")
    List<String> voiceUrls,

    @Schema(description = "메타데이터")
    String metadata,

    @Schema(description = "생성일시", example = "2024-08-21T03:47:23.328108")
    LocalDateTime createdAt,

    @Schema(description = "수정일시", example = "2024-08-21T03:47:23.328108")
    LocalDateTime updatedAt,

    @Schema(description = "태그 리스트")
    List<TagResponse> tags
) {

    public static MemoResponse fromRawTag(Memo memo, List<RawTag> tags) {
        return new MemoResponse(
            memo.getId(),
            memo.getContent(),
            memo.getImageUrls(),
            memo.getVoiceUrls(),
            memo.getMetadata(),
            memo.getCreatedAt(),
            memo.getUpdatedAt(),
            tags.stream().map(TagResponse::fromRawTag).toList()
        );
    }

    public static MemoResponse fromTag(Memo memo, List<Tag> tags) {
        return new MemoResponse(
            memo.getId(),
            memo.getContent(),
            memo.getImageUrls(),
            memo.getVoiceUrls(),
            memo.getMetadata(),
            memo.getCreatedAt(),
            memo.getUpdatedAt(),
            tags.stream().map(TagResponse::fromTag).toList()
        );
    }
}
