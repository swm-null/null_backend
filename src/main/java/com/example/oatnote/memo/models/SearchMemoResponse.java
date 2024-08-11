package com.example.oatnote.memo.models;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import java.util.List;

import com.example.oatnote.memo.models.InnerResponse.MemoResponse;
import com.example.oatnote.memo.service.memo.models.Memo;
import com.example.oatnote.memo.service.tag.models.Tag;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(SnakeCaseStrategy.class)
public record SearchMemoResponse(
    @Schema(description = "자연어 응답", example = "일정과 관련된 메모를 찾아드리겠습니다.")
    String processedMessage,

    @Schema(description = "메모 리스트")
    List<MemoResponse> memos
) {

    public static SearchMemoResponse from(String processedMessage, List<Memo> memos, List<List<Tag>> tagsList) {
        return new SearchMemoResponse(
            processedMessage,
            memos.stream()
                .map(memo -> MemoResponse.from(memo, tagsList.get(memos.indexOf(memo))))
                .toList()
        );
    }
}
