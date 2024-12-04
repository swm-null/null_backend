package com.example.oatnote.memotag.dto;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import java.util.List;

import com.example.oatnote.memotag.dto.innerDto.MemoResponse;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(SnakeCaseStrategy.class)
public record SearchMemosUsingAiResponse(
    @Schema(description = "자연어 응답", example = "일정과 관련된 메모를 찾아드리겠습니다.")
    String processedMessage,

    @Schema(description = "메모 리스트")
    List<MemoResponse> memos
) {

    public static SearchMemosUsingAiResponse from(String processedMessage, List<MemoResponse> memos) {
        return new SearchMemosUsingAiResponse(
            processedMessage,
            memos
        );
    }
}
