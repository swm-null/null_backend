package com.example.oatnote.memotag.eventlistener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.oatnote.event.CreateStructureAsyncEvent;
import com.example.oatnote.memotag.MemoTagService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CreateStructureAsyncEventListener {

    private final MemoTagService memoTagService;

    @EventListener
    public void handelCreateStructureAsyncEventEvent(CreateStructureAsyncEvent event) {
        memoTagService.createStructureAsync(
            event.aiCreateTagsResponse(),
            event.rawMemo(),
            event.userId(),
            event.now()
        );
    }
}
