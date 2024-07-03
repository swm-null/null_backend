package com.example.memo.memo.service.models.bridge;

import java.util.List;

import com.example.memo.memo.service.models.Memo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemoRequestBridge {

    String content;

    public static Memo toMemo(String memoId, List<String> tags, String content) {
        return Memo.builder()
            .id(memoId)
            .tags(tags)
            .content(content)
            .build();
    }
}
