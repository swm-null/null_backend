package com.example.memo.memo.service.models.bridge;

import java.util.List;

import com.example.memo.memo.service.models.Memo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateMemoResponseBridge {

    String id;
    String content;
    List<String> tags;

    public static UpdateMemoResponseBridge from(Memo memo) {
        return new UpdateMemoResponseBridge(
            memo.getId(),
            memo.getContent(),
            memo.getTags()
        );
    }
}
