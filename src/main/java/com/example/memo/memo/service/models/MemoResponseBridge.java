package com.example.memo.memo.service.models;

import java.util.List;

import lombok.Getter;

@Getter
public class MemoResponseBridge {

    String id;
    String content;
    List<String> tags;

    public MemoResponseBridge(String id, String content, List<String> tags) {
        this.id = id;
        this.content = content;
        this.tags = tags;
    }

    public static MemoResponseBridge from(Memo memo) {
        return new MemoResponseBridge(
            memo.getId(),
            memo.getContent(),
            memo.getTags()
        );
    }
}
