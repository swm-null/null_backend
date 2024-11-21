package com.example.oatnote.domain.memotag.dto;

import static com.example.oatnote.web.validation.enums.AllowedFileTypeEnum.VOICE;
import static com.example.oatnote.web.validation.enums.AllowedFileTypeEnum.IMAGE;
import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

import java.util.List;

import com.example.oatnote.domain.memotag.service.client.dto.AiCreateTagsRequest;
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
    @Size(max = 1000, message = "내용은 최대 1000자까지 입력 가능합니다.")
    String content,

    @Schema(description = "이미지 URL 리스트", example = "[\"https://example.com/image1.jpg\"]", requiredMode = REQUIRED)
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

    public Memo toRawMemo(String userId, String metadata) {
        return new Memo(
            content,
            imageUrls,
            voiceUrls,
            userId,
            metadata
        );
    }
}
