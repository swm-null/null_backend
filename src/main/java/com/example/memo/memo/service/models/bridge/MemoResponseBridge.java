package com.example.memo.memo.service.models.bridge;

import java.util.List;

import com.example.memo.memo.service.models.Memo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemoResponseBridge {

    String id;
    String content;
    List<String> tags;

    public static MemoResponseBridge from(Memo memo) {
        return new MemoResponseBridge(
            memo.getId(),
            memo.getContent(),
            memo.getTags()
        );
    }
}
