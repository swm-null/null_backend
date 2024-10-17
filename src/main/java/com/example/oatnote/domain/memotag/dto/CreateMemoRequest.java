package com.example.oatnote.domain.memotag.dto;

import static com.example.oatnote.web.validation.enums.AllowedFileTypeEnum.*;
import static com.example.oatnote.web.validation.enums.AllowedFileTypeEnum.SVG;
import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import java.time.LocalDateTime;
import java.util.List;

import com.example.oatnote.domain.memotag.service.client.dto.AICreateTagsRequest;
import com.example.oatnote.domain.memotag.service.memo.model.Memo;
import com.example.oatnote.web.validation.AllowedFileType;
import com.example.oatnote.web.validation.MemoAtLeastOneRequired;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@JsonNaming(SnakeCaseStrategy.class)
@MemoAtLeastOneRequired
public record CreateMemoRequest(
    @Schema(description = "내용", example = "내일은 5시 멘토링을 들어야해", requiredMode = REQUIRED)
    @NotNull(message = "내용은 null일 수 없습니다.")
    String content,

    @Schema(
        description = "이미지 URL 리스트",
        example = "[\"https://example.com/image1.jpg\", \"https://example.com/image2.jpg\"]",
        requiredMode = REQUIRED
    )
    @NotNull(message = "이미지 URL 리스트는 null일 수 없습니다.")
    @Size(max = 5, message = "이미지 URL 리스트는 5개 이하로 입력해주세요.")
    @AllowedFileType(IMAGE)
    List<String> imageUrls
) {

    public AICreateTagsRequest toAICreateMemoRequest(String userId) {
        return new AICreateTagsRequest(
            new AICreateTagsRequest.RawMemo(content, imageUrls),
            userId
        );
    }

    public Memo toRawMemo(String userId, LocalDateTime time) {
        return new Memo(
            content,
            imageUrls,
            userId,
            time
        );
    }
}
