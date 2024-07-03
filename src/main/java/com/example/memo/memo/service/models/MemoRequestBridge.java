package com.example.memo.memo.service.models;

import java.util.List;

import lombok.Getter;

@Getter
public class MemoRequestBridge {

    String content;

    public MemoRequestBridge(String content) {
        this.content = content;
    }

    public static Memo toMemo(String memoId, List<String> tags, String content) {
        return Memo.builder()
            .id(memoId)
            .tags(tags)
            .content(content)
            .build();
    }
}
