package com.example.memo.memo.service.models;

import lombok.Getter;

@Getter
public class MemoRequestBridge {

    private String content;

    public MemoRequestBridge(String content) {
        this.content = content;
    }

    public static Memo toMemo(MemoRequestBridge memoRequestBridge) {
        return Memo.builder()
            .content(memoRequestBridge.getContent())
            .tags(null)
            .build();
    }
}
