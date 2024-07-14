package com.example.memo.memo.models;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

import java.util.List;

import org.bson.types.ObjectId;

import com.example.memo.memo.service.models.Memo;
import com.example.memo.memo.service.models.Tag;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(SnakeCaseStrategy.class)
public record SearchMemoResponse(
    @Schema(description = "자연어 응답", example = "일정과 관련된 메모를 찾아드리겠습니다.")
    String processedMessage,

    @Schema(description = "메모 리스트")
    List<InnerMemo> memos
) {

    @JsonNaming(SnakeCaseStrategy.class)
    public record InnerMemo(
        @Schema(description = "메모 고유 ID", example = "61b72b3e9b1e8b1e4c8b4560")
        ObjectId id,

        @Schema(description = "내용", example = "내일은 5시 멘토링을 들어야해")
        String content,

        @Schema(description = "태그", example = """
            [
                {"id": "60c72b3e9b1e8b1e4c8b4568", "name": "일정"},
                {"id": "60c72b4f9b1e8b1e4c8b4569", "name": "멘토링"}
            ]
            """)
        List<InnerTag> tags
    ) {

        public record InnerTag(
            @Schema(description = "태그 ID", example = "60c72b3e9b1e8b1e4c8b4568")
            ObjectId id,

            @Schema(description = "태그 이름", example = "일정")
            String name
        ) {
        }

        public static InnerMemo from(Memo memo, List<Tag> tags) {
            return new InnerMemo(
                memo.getId(),
                memo.getContent(),
                tags.stream()
                    .map(tag -> new InnerTag(tag.getId(), tag.getName()))
                    .toList()
            );
        }
    }

    public static SearchMemoResponse from(String processedMessage, List<Memo> memos, List<List<Tag>> tagsList) {
        List<InnerMemo> innerMemos = memos.stream()
            .map(memo -> InnerMemo.from(memo, tagsList.get(memos.indexOf(memo))))
            .toList();
        return new SearchMemoResponse(processedMessage, innerMemos);
    }
}
