package com.example.memo.memo.models;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import java.util.List;

import com.example.memo.memo.service.models.Memo;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(SnakeCaseStrategy.class)
public record SearchMemoResponse(
    @Schema(description = "자연어 응답", example = "자연어 응답입니다.")
    String processedMessage,

    @Schema(description = "메모 리스트")
    List<InnerMemo> memos
) {

    @JsonNaming(SnakeCaseStrategy.class)
    public static record InnerMemo(
        @Schema(description = "메모 고유 ID", example = "1")
        String id,

        @Schema(description = "내용", example = "text")
        String content,

        @Schema(description = "태그", example = """
            ["tag1", "tag2"]
            """)
        List<String> tags
    ) {
        public static InnerMemo from(Memo memo) {
            return new InnerMemo(
                memo.getId(),
                memo.getContent(),
                memo.getTags()
            );
        }
    }

    public static SearchMemoResponse from(String processedMessage, List<Memo> memos) {
        return new SearchMemoResponse(
            processedMessage,
            memos.stream().map(InnerMemo::from).toList()
        );
    }
}
