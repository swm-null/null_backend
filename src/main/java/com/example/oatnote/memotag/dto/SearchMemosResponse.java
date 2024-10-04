package com.example.oatnote.memotag.dto;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import java.util.List;

import com.example.oatnote.memotag.dto.innerDto.MemoResponse;
import com.example.oatnote.memotag.service.memo.model.Memo;
import com.example.oatnote.memotag.service.tag.model.Tag;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(SnakeCaseStrategy.class)
public record SearchMemosResponse(
    @Schema(description = "자연어 응답", example = "일정과 관련된 메모를 찾아드리겠습니다.")
    String processedMessage,

    @Schema(description = "메모 리스트")
    List<MemoResponse> memos
) {

    public static SearchMemosResponse from(String processedMessage, List<Memo> memos, List<List<Tag>> tagsList) {
        return new SearchMemosResponse(
            processedMessage,
            memos.stream()
                .map(memo -> MemoResponse.fromTag(memo, tagsList.get(memos.indexOf(memo))))
                .toList()
        );
    }
}
