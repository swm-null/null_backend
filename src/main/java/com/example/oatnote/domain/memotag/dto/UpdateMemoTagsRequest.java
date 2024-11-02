package com.example.oatnote.domain.memotag.dto;

import static com.example.oatnote.web.validation.enums.AllowedFileTypeEnum.IMAGE;
import static com.example.oatnote.web.validation.enums.AllowedFileTypeEnum.VOICE;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import java.time.LocalDateTime;
import java.util.List;

import com.example.oatnote.domain.memotag.service.client.dto.AiCreateTagsRequest;
import com.example.oatnote.domain.memotag.service.memo.model.Memo;
import com.example.oatnote.web.validation.AllowedFileType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@JsonNaming(SnakeCaseStrategy.class)
public record UpdateMemoTagsRequest(
    @Schema(description = "업데이트할 내용", example = "내일 5시로 멘토링이 변경되었다.", requiredMode = REQUIRED)
    @NotNull(message = "내용은 null일 수 없습니다.")
    @Size(max = 2000, message = "내용은 최대 2000자까지 입력 가능합니다.")
    String content,

    @Schema(
        description = "업데이트할 이미지 URL 리스트",
        example = "[\"https://example.com/image1.jpg\", \"https://example.com/image2.jpg\"]",
        requiredMode = REQUIRED
    )
    @NotNull(message = "이미지 URL 리스트는 null일 수 없습니다.")
    @Size(max = 5, message = "이미지 URL 리스트는 5개 이하로 입력해주세요.")
    @AllowedFileType(IMAGE)
    List<String> imageUrls,

    @Schema(description = "음성 URL 리스트", example = "[\"https://example.com/voice1.mp3\"]")
    @NotNull(message = "음성 URL 리스트는 null일 수 없습니다.")
    @Size(max = 1, message = "이미지 URL 리스트는 1개 이하로 입력해주세요.")
    @AllowedFileType(VOICE)
    List<String> voiceUrls
) {

    public AiCreateTagsRequest toAiCreateMemoRequest(String userId) {
        return new AiCreateTagsRequest(
            new AiCreateTagsRequest.RawMemo(content, imageUrls, voiceUrls),
            userId
        );
    }

    public Memo toRawMemo(String memoId, String userId, LocalDateTime now) {
        return new Memo(
            memoId,
            content,
            imageUrls,
            voiceUrls,
            userId,
            null,
            now
        );
    }
}
